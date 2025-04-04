package com.booking_hotel.api.hotel.service;

import static com.booking_hotel.api.utils.messageUtils.MessageUtils. *;

import com.booking_hotel.api.auth.config.jwt.JwtProvider;
import com.booking_hotel.api.auth.entity.User;
import com.booking_hotel.api.auth.service.user.UserService;
import com.booking_hotel.api.exception.ElementNotFoundException;
import com.booking_hotel.api.hotel.dto.CountByCityResponse;
import com.booking_hotel.api.hotel.dto.HotelResponse;
import com.booking_hotel.api.hotel.entity.Hotel;
import com.booking_hotel.api.hotel.repository.HotelRepository;
import com.booking_hotel.api.utils.dtoUtils.HotelResponseUtils;
import com.booking_hotel.api.role.entity.Role;
import com.booking_hotel.api.utils.roleUtils.RoleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;

    private final UserService userService;

    @Override
    public ResponseEntity<HotelResponse> createHotel(Hotel hotel, String token) {
        Optional<User> userOptional = userService.findByUsername(JwtProvider.getUserNameByToken(token));
        Set<Role> roles = userOptional.get().getRoles();
        boolean isBusinessOwner = false;

        for (Role role : roles) {
            if(role.getRoleName().equals(RoleUtils.ROLE_OWNER)) {
                isBusinessOwner = true;
            }
        }
        if(isBusinessOwner){
            Hotel newHotel = Hotel.builder()
                    .hotelId(hotel.getHotelId())
                    .name(hotel.getName())
                    .address(hotel.getAddress())
                    .city(hotel.getCity())
                    .country(hotel.getCountry())
                    .description(hotel.getDescription())
                    .cheapestPrice(hotel.getCheapestPrice())
                    .owner(userOptional.get())
                    .build();

            Hotel hotelSaved = hotelRepository.save(newHotel);
            HotelResponse response = HotelResponseUtils.buildHotelResponse(hotelSaved);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        throw new AccessDeniedException(ROLE_INVALID_MESSAGE);
    }

    @Override
    public List<HotelResponse> getAllHotels(Pageable pageable) {
        Page<Hotel> hotels = hotelRepository.findAll(pageable);
        List<Hotel> hotelList = hotels.getContent();
        return convertToHotelResponseList(hotelList);
    }

    @Override
    public ResponseEntity<List<HotelResponse>> getAllHotelsLimit(int limit) {
        List<Hotel> hotels = hotelRepository.findAll().stream().limit(limit).collect(Collectors.toList());
        return ResponseEntity.ok(convertToHotelResponseList(hotels));
    }

    @Override
    public ResponseEntity<CountByCityResponse> countByCity() {
        Map<String, Integer> countByCityMap = new HashMap<>();
        List<Hotel> hotels = hotelRepository.findAll();

        for(Hotel hotel : hotels) {
            countByCityMap.put(hotel.getCity(), countByCityMap.getOrDefault(hotel.getCity(), 0) + 1);
        }

        CountByCityResponse countByCityResponse = CountByCityResponse.builder()
                                                                    .countByCityMap(countByCityMap)
                                                                    .build();
        return new ResponseEntity<>(countByCityResponse, HttpStatus.OK);
    }

    @Override
    public Optional<Hotel> getHotelById(Long id) {
        return hotelRepository.findById(id);
    }

    @Override
    public ResponseEntity<?> updateHotel(Long id, Hotel hotelDetail) {
        System.out.println(hotelDetail);
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new RuntimeException("Hotel not found"));
        hotel.setName(hotelDetail.getName());
        hotel.setAddress(hotelDetail.getAddress());
        hotel.setCity(hotelDetail.getCity());
        hotel.setCountry(hotelDetail.getCountry());
        hotel.setDescription(hotelDetail.getDescription());

        hotelRepository.save(hotel);

        return ResponseEntity.ok("Updated Hotel");

    }

    @Override
    public void deleteHotel(Long id) {
        hotelRepository.deleteById(id);
    }

    @Override
    public List<HotelResponse> convertToHotelResponseList(List<Hotel> hotels) {
        return hotels.stream()
                .map(HotelResponseUtils::buildHotelResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<HotelResponse> searchHotelsWithSpecification(Specification<Hotel> specification) {
        List<Hotel> hotels = hotelRepository.findAll(specification);
        return convertToHotelResponseList(hotels);
    }

    @Override
    public ResponseEntity<List<HotelResponse>> createHotels(List<Hotel> hotels, String token) {
        List<HotelResponse> hotelResponseList = new ArrayList<>();
        for (Hotel hotel : hotels) {
            HotelResponse hotelResponse = createHotel(hotel, token).getBody();
            hotelResponseList.add(hotelResponse);
        }
        return new ResponseEntity<>(hotelResponseList, HttpStatus.CREATED);
    }
}
