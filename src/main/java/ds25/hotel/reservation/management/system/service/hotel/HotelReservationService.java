package ds25.hotel.reservation.management.system.service.hotel;

import ds25.hotel.reservation.management.system.configuration.Singleton;
import ds25.hotel.reservation.management.system.entity.hotel.HotelReservation;
import ds25.hotel.reservation.management.system.repository.hotel.HotelReservationRepository;
import ds25.hotel.reservation.management.system.repository.hotel.HotelRoomRepository;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;

@Slf4j
public class HotelReservationService {
    HotelReservationRepository hotelReservationRepository = Singleton.getInstance().getHotelReservationRepository();

    /**
     * 호텔 예약 추가
     *
     * @param hotelReservation 호텔 예약 정보
     * @return 호텔 예약 정보
     * @throws IOException 파일 입출력 예외
     * @author 김남주
     */
    public HotelReservation addHotelReservation(HotelReservation hotelReservation) throws Exception {
        if (hotelReservation.getIdx() == 0) {
            log.info("호텔 예약이 추가되었습니다");
            return hotelReservationRepository.save(hotelReservation);
        } else {
            log.error("이미 존재하는 호텔 예약 정보입니다");
            throw new Exception("이미 존재하는 호텔 예약 정보입니다");
        }
    }

    /**
     * 호텔 예약 정보 수정
     *
     * @param hotelReservation 호텔 예약 정보
     * @throws IOException 파일 입출력 예외
     * @author 김남주
     */
    public HotelReservation updateHotelReservation(HotelReservation hotelReservation) throws IOException {
        Optional<HotelReservation> oldHotelReservation = hotelReservationRepository.findById(hotelReservation.getIdx());
        if (oldHotelReservation.isPresent()) {
            log.info("호텔 예약이 수정되었습니다");
            return hotelReservationRepository.save(hotelReservation);
        } else {
            log.error("존재하지 않는 호텔 예약 정보입니다");
            return null;
        }
    }

    /**
     * 호텔 예약 삭제
     *
     * @param hotelReservationIdx 호텔 예약 index 번호
     * @throws IOException 파일 입출력 예외
     * @author 김남주
     */
    public void removeHotelReservation(int hotelReservationIdx) throws Exception {
        Optional<HotelReservation> oldHotelReservation = hotelReservationRepository.findById(hotelReservationIdx);
        if (oldHotelReservation.isPresent()) {
            log.info("호텔 예약이 삭제되었습니다");
            hotelReservationRepository.deleteById(hotelReservationIdx);
        } else {
            log.error("존재하지 않는 호텔 예약 정보입니다");
            throw new Exception("존재하지 않는 호텔 예약 정보입니다");
        }
    }

    /**
     * 특정 호텔 예약 내역 조회
     *
     * @param hotelReservationIdx 호텔 예약 index 번호
     * @return 호텔 예약 정보
     * @throws IOException 파일 입출력 예외
     * @author 김남주
     */
    public HotelReservation getHotelReservation(int hotelReservationIdx) throws Exception {
        Optional<HotelReservation> oldHotelReservation = hotelReservationRepository.findById(hotelReservationIdx);
        if (oldHotelReservation.isPresent()) {
            log.info("호텔 예약 정보를 조회합니다");
            return oldHotelReservation.get();
        } else {
            log.error("존재하지 않는 호텔 예약 정보입니다");
            throw new Exception("존재하지 않는 호텔 예약 정보입니다");
        }
    }

    /**
     * 호텔 객실 별 예약 내역 조회
     *
     * @param hotelRoomIdx 호텔 방 index 번호
     * @return 호텔 예약 정보
     * @throws IOException 파일 입출력 예외
     * @author 김남주
     */
    public ArrayList<HotelReservation> getHotelReservationByHotelRoomIdx(int hotelRoomIdx) throws IOException {
        log.info("호텔 객실 별 예약 내역을 조회합니다");
        return hotelReservationRepository.findByHotelRoomId(hotelRoomIdx);
    }

    /**
     * 유저 별 호텔 예약 내역 조회
     *
     * @param userId 사용자 idx
     * @return 호텔 예약 정보
     * @throws IOException 파일 입출력 예외
     * @author 김남주
     */
    public ArrayList<HotelReservation> getHotelReservationByUserId(int userId) throws IOException {
        log.info("유저 별 호텔 예약 내역을 조회합니다");
        return hotelReservationRepository.findByUserId(userId);
    }

    /**
     * 호텔 별 호텔 예약 내역 조회
     *
     * @param hotelId 호텔 idx
     * @return 호텔 예약 정보
     * @throws IOException 파일 입출력 예외
     * @author 김남주
     */
    public ArrayList<HotelReservation> getHotelReservationByHotelId(int hotelId) throws IOException {
        log.info("호텔 별 호텔 예약 내역을 조회합니다");
        return hotelReservationRepository.findByHotelId(hotelId);
    }

    /**
     * 예약 날짜 내에 사용중인 객실 수를 반환
     *
     * @param hotelRoomIdx 호텔 객실 idx
     * @param start        예약 시작 날짜
     * @param end          예약 종료 날짜
     * @return 사용중인 객실 수
     * @throws IOException 파일 입출력 예외
     * @author 김남주
     */
    public int getUsingRoomCount(int hotelRoomIdx, Date start, Date end) throws IOException {
        ArrayList<HotelReservation> hotelReservations = hotelReservationRepository.findByHotelReservationsByRoomIdxAndTime(hotelRoomIdx, start, end);

        for (HotelReservation hotelReservation : hotelReservations) {

            log.info(hotelReservation.toString());
        }

        int availableRoomCount = 0;

        if (hotelReservations.isEmpty()) {
            return 0;
        }

        hotelReservations.sort(Comparator.comparing(HotelReservation::getCheckOutDate));
        PriorityQueue<HotelReservation> priorityQueue = new PriorityQueue<>(Comparator.comparing(HotelReservation::getCheckOutDate));

        priorityQueue.add(hotelReservations.get(0));
        for (int i = 1; i < hotelReservations.size(); i++) {
            HotelReservation hotelReservation = hotelReservations.get(i);
            HotelReservation peek = priorityQueue.peek();
            if (hotelReservation.getCheckInDate().after(peek.getCheckOutDate())) {
                priorityQueue.poll();
            }
            priorityQueue.add(hotelReservation);
        }

        return priorityQueue.size();
    }

}