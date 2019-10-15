package com.pefthymiou.mars.booking.domain.actions.create;

import com.pefthymiou.mars.booking.domain.CreateBookingRequestDto;
import com.pefthymiou.mars.booking.infrastructure.db.Booking;
import com.pefthymiou.mars.booking.infrastructure.db.BookingRepository;
import com.pefthymiou.mars.notifications.domain.NotificationCreationException;
import com.pefthymiou.mars.notifications.domain.actions.send.NotificationsService;
import com.pefthymiou.mars.unit.domain.RequestDto;
import com.pefthymiou.mars.unit.domain.UnitNotFoundException;
import com.pefthymiou.mars.unit.domain.UnprocessableRequestException;
import com.pefthymiou.mars.unit.infrastructure.db.Unit;
import com.pefthymiou.mars.unit.infrastructure.db.UnitRepository;
import com.pefthymiou.mars.user.infrastructure.db.AppUser;
import com.pefthymiou.mars.user.infrastructure.db.AppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class BookingUnitService implements BookingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingUnitService.class);

    private static final String UNIT_NOT_FOUND_MESSAGE = "Requested unit was not found";
    private static final String UNIT_UNAVAILABLE = "Requested unit is unavailable for the selected dates";
    private static final String COULD_NOT_FETCH_USER_WITH = "Could not fetch user with id ";
    private static final String COULD_NOT_FETCH_UNIT_WITH = "Could not fetch unit with id ";
    private static final String COULD_NOT_CREATE_NOTIFICATION = "Could not create notification: ";

    private BookingRepository bookingRepository;
    private UnitRepository unitRepository;
    private BookingValidator bookingValidator;
    private BookingMapper bookingMapper;
    private AppUserRepository userRepository;
    private NotificationsService notificationsService;

    @Autowired
    public BookingUnitService(
            BookingRepository bookingRepository,
            UnitRepository unitRepository,
            BookingValidator bookingValidator,
            BookingMapper bookingMapper,
            AppUserRepository userRepository,
            NotificationsService notificationsService
    ) {
        this.bookingRepository = bookingRepository;
        this.unitRepository = unitRepository;
        this.bookingValidator = bookingValidator;
        this.bookingMapper = bookingMapper;
        this.userRepository = userRepository;
        this.notificationsService = notificationsService;
    }

    @Override
    public long createFrom(RequestDto request) {
        bookingValidator.validate(request);
        Optional<Unit> unit = unitRepository.findById(((CreateBookingRequestDto) request).getUnitId());

        if (unit.isPresent()) {
            checkAvailabilityFor((CreateBookingRequestDto) request, unit.get().getTimezone());
            Booking booking = bookingMapper.mapFrom((CreateBookingRequestDto) request, unit.get().getTimezone());
            Booking savedBooking = bookingRepository.save(booking);
            createNotificationFor(booking);
            return savedBooking.getId();
        } else {
            throw new UnitNotFoundException(UNIT_NOT_FOUND_MESSAGE);
        }
    }

    private void checkAvailabilityFor(CreateBookingRequestDto request, String timezone) {
        Iterable<Booking> bookingsForSameDays = bookingRepository.bookingsWithin(
                bookingMapper.timestampFrom(request.getCheckIn(), timezone),
                bookingMapper.timestampFrom(request.getCheckOut(), timezone)
        );
        if (!((Collection) bookingsForSameDays).isEmpty())
            throw new UnprocessableRequestException(UNIT_UNAVAILABLE);
    }

    private void createNotificationFor(Booking booking) {
        try {
            notificationsService.createWith(userEmailFrom(booking), unitTitleFrom(booking), booking.getCheckIn());
        } catch (NotificationCreationException e) {
            LOGGER.error(COULD_NOT_CREATE_NOTIFICATION + e.getMessage());
        }
    }

    private String userEmailFrom(Booking booking) {
        Optional<AppUser> user = userRepository.findById(booking.getUserId());
        if (user.isPresent())
            return user.get().getEmail();
        else
            throw new NotificationCreationException(COULD_NOT_FETCH_USER_WITH + booking.getUserId());
    }

    private String unitTitleFrom(Booking booking) {
        Optional<Unit> unit = unitRepository.findById(booking.getUnitId());
        if (unit.isPresent())
            return unit.get().getTitle();
        else
            throw new NotificationCreationException(COULD_NOT_FETCH_UNIT_WITH + booking.getUnitId());
    }
}
