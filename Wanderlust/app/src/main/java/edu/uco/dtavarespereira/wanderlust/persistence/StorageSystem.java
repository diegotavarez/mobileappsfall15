package edu.uco.dtavarespereira.wanderlust.persistence;

import java.util.List;

import edu.uco.dtavarespereira.wanderlust.entity.Place;

/**
 * Created by diegotavarez on 12/1/15.
 */
public interface StorageSystem {

    void addPlace(Place place);
    void deletePlace(Integer id);
    void edit(Place place);
    Place getPlace(int key);
    List<Place> getPlaces();
}
