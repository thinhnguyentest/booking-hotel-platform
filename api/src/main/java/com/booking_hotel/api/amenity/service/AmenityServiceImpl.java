package com.booking_hotel.api.amenity.service;

import com.booking_hotel.api.amenity.dto.AmenityResponse;
import com.booking_hotel.api.amenity.entity.Amenity;
import com.booking_hotel.api.amenity.repository.AmenityRepository;
import com.booking_hotel.api.hotel.entity.Hotel;
import com.booking_hotel.api.hotel.service.HotelService;
import static com.booking_hotel.api.utils.dtoUtils.AmenityResponseUtils. *;
import com.booking_hotel.api.utils.messageUtils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AmenityServiceImpl implements AmenityService {

    private final AmenityRepository amenityRepository;
    private final HotelService hotelService;

    @Override
    public void deleteAmenity(Long id) {
        Amenity amenity = amenityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Amenity not found"));
        amenityRepository.delete(amenity);
    }

    @Override
    public Amenity updateAmenity(Long id, Amenity amenityDetails) {
        Amenity amenity = amenityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Amenity not found"));

        amenity.setAmenityName(amenityDetails.getAmenityName());
        amenity.setAmenityDescription(amenityDetails.getAmenityDescription());
        amenity.setHotel(amenityDetails.getHotel());

        return amenityRepository.save(amenity);
    }

    @Override
    public AmenityResponse createAmenity(Amenity amenity, Long hotelId) {
        Optional<Hotel> hotel = hotelService.getHotelById(hotelId);
        if(hotel.isEmpty()) {
            throw new RuntimeException(MessageUtils.NOT_FOUND_HOTEL_MESSAGE);
        }
        Amenity newAmenity = Amenity.builder()
                                    .amenityName(amenity.getAmenityName())
                                    .amenityDescription(amenity.getAmenityDescription())
                                    .hotel(hotel.get())
                                    .build();
        return buildImageResponse(amenityRepository.save(newAmenity));
    }

    @Override
    public List<AmenityResponse> createAmenites(List<Amenity> amenities, Long hotelId) {
        List<AmenityResponse> amenitiesSavedList = new ArrayList<>();
        for (Amenity amenity : amenities) {
            amenitiesSavedList.add(createAmenity(amenity, hotelId));
        }
        return amenitiesSavedList;
    }

    @Override
    public Optional<AmenityResponse> getAmenityById(Long id) {
        return Optional.of(buildImageResponse(amenityRepository.findById(id).get()));
    }

    @Override
    public List<AmenityResponse> getAllAmenities() {
        return convertToImageResponseList(amenityRepository.findAll());
    }
}
