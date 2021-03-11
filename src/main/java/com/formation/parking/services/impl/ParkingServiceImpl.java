package com.formation.parking.services.impl;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.formation.parking.dao.ParkingAPIDAO;
import com.formation.parking.dao.entity.RecordEntity;
import com.formation.parking.dao.entity.ResponseParkingAPIEntity;
import com.formation.parking.models.Parking;
import com.formation.parking.services.ParkingService;

@Service
public class ParkingServiceImpl implements ParkingService {

	@Autowired
	private ParkingAPIDAO parkingAPIDAO;

	@Override
	public List<Parking> getListParkings() {
		ResponseParkingAPIEntity response = parkingAPIDAO.getListParking();

		return transformEntityToModel(response);
	}

	private List<Parking> transformEntityToModel(ResponseParkingAPIEntity response) {
		List<Parking> list = new ArrayList<Parking>();

		for (RecordEntity record : response.getRecords()) {
			Parking parking = new Parking();

			parking.setNom(record.getFields().getGrpNom());
			parking.setNbPlacesDispo(record.getFields().getGrpDisponible());
			parking.setNbPlacesTotal(record.getFields().getGrpExploitation());
			parking.setHeureMaj(getHeureMaj(record));
			parking.setStatut(getLibelleStatut(record));

			list.add(parking);
		}

		return list;
	}

	private String getHeureMaj(RecordEntity record) {
		ZonedDateTime dateMaj = ZonedDateTime.parse(record.getFields().getGrpHorodatage());
		
		ZoneId parisZoneId = ZoneId.of("Europe/Paris");
		ZonedDateTime dateMajLocal = dateMaj.withZoneSameInstant(parisZoneId);
		
		return dateMajLocal.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
	}

	private String getLibelleStatut(RecordEntity record) {
		switch (record.getFields().getGrpStatut()) {
			case "1": {
				return "FERME";
			}
			case "2": {
				return "ABONNES";
			}
			case "5": {
				return "OUVERT";
			}
		}
		return "Données non disponibles";
	}

}
