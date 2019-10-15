package com.pefthymiou.mars.dsl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.pefthymiou.mars.booking.infrastructure.db.Booking;
import com.pefthymiou.mars.booking.infrastructure.db.BookingRepository;
import com.pefthymiou.mars.notifications.infrastructure.db.Notification;
import com.pefthymiou.mars.notifications.infrastructure.db.NotificationRepository;
import com.pefthymiou.mars.user.infrastructure.db.AppUser;
import com.pefthymiou.mars.user.infrastructure.db.AppUserRepository;
import com.pefthymiou.mars.user.infrastructure.db.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashSet;

import static com.pefthymiou.mars.security.configuration.SecurityParams.*;
import static java.util.Collections.singletonList;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
public class MarsDsl {

    @Value("${api.basepath}")
    private String apiBasepath;

    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private BookingRepository bookingRepository;

    public MvcResult createAUnit(UnitDsl.ITUnit unit, MockMvc mockMvc) throws Exception {
        return mockMvc.perform(post(apiBasepath + "/unit")
                .header("Authorization", createAdminToken())
                .contentType(APPLICATION_JSON_UTF8_VALUE)
                .content(Parser.jsonWith(unit)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn();
    }

    public void create(UserDsl.ITUser user) {
        appUserRepository.save(
                new AppUser(
                        user.getUsername(),
                        user.getPassword(),
                        user.getEmail(),
                        user.getTimezone(),
                        new HashSet<>(singletonList(new Role("ADMIN", "administrator")))
                )
        );
    }

    public void create(NotificationDsl.ITNotification notification) {
        notificationRepository.save(
                new Notification(
                        notification.getEmail(),
                        notification.getUnitTitle(),
                        notification.getCheckIn(),
                        notification.getDueDate()
                )
        );
    }

    public void create(BookingDsl.ITBooking booking) {
        bookingRepository.save(
                new Booking(
                        booking.getUserId(),
                        booking.getUnitId(),
                        new Timestamp(
                                ZonedDateTime.parse(booking.getCheckIn() + "T00:00:00+00:00[Europe/London]")
                                        .toInstant()
                                        .toEpochMilli()
                        ),
                        new Timestamp(
                                ZonedDateTime.parse(booking.getCheckOut() + "T00:00:00+00:00[Europe/London]")
                                        .toInstant()
                                        .toEpochMilli()
                        )
                )
        );
    }

    public String extractIdFrom(MvcResult response) {
        String location = response.getResponse().getHeader("Location");
        return location.split("/")[location.split("/").length - 1];
    }

    public String extractLocationFrom(MvcResult response) {
        return response.getResponse().getHeader("Location");
    }

    public String createAdminToken() {
        return "Bearer " + JWT.create()
                .withSubject("bob")
                .withIssuedAt(Date.from(Instant.now().atZone(ZoneId.systemDefault()).toInstant()))
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .withArrayClaim(ROLES_KEY, new String[]{"ADMIN"})
                .sign(Algorithm.HMAC512(SECRET.getBytes()));
    }

    public String createColonistToken() {
        return "Bearer " + JWT.create()
                .withSubject("alice")
                .withIssuedAt(Date.from(Instant.now().atZone(ZoneId.systemDefault()).toInstant()))
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .withArrayClaim(ROLES_KEY, new String[]{"COLONIST"})
                .sign(Algorithm.HMAC512(SECRET.getBytes()));
    }
}
