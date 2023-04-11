package ds25.hotel.reservation.management.system.entity.hotel;

import ds25.hotel.reservation.management.system.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long idx;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    private HotelRoom hotelRoom;
    private Timestamp checkInDate;
    private Timestamp checkOutDate;
    private long peopleCount;
    private long totalPrice;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private int payedMoney;
}
