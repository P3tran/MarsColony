package com.pefthymiou.mars.booking;

import com.pefthymiou.mars.base.builders.BookingBuilder;
import com.pefthymiou.mars.booking.domain.CreateBookingRequestDto;
import com.pefthymiou.mars.booking.domain.actions.create.BookingMapper;
import com.pefthymiou.mars.booking.domain.actions.create.BookingUnitService;
import com.pefthymiou.mars.booking.domain.actions.create.BookingValidator;
import com.pefthymiou.mars.booking.infrastructure.db.Booking;
import com.pefthymiou.mars.booking.infrastructure.db.BookingRepository;
import com.pefthymiou.mars.notifications.domain.actions.send.BookingNotificationService;
import com.pefthymiou.mars.unit.domain.InvalidRequestException;
import com.pefthymiou.mars.unit.domain.UnitNotFoundException;
import com.pefthymiou.mars.unit.domain.UnprocessableRequestException;
import com.pefthymiou.mars.unit.infrastructure.db.Unit;
import com.pefthymiou.mars.unit.infrastructure.db.UnitRepository;
import com.pefthymiou.mars.user.infrastructure.db.AppUser;
import com.pefthymiou.mars.user.infrastructure.db.AppUserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Optional;

import static com.pefthymiou.mars.base.builders.AppUserBuilder.aUser;
import static com.pefthymiou.mars.base.builders.UnitBuilder.aUnit;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class BookUnitServiceShould {

    private static final long UNIT_ID = 2;
    private static final Booking BOOKING = BookingBuilder.aBooking().build();
    private static final String INVALID_REQUEST = "Invalid request";
    private static final AppUser USER = aUser().build();
    private static final Unit UNIT = aUnit().build();

    private BookingUnitService bookUnitService;

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UnitRepository unitRepository;
    @Mock
    private BookingValidator bookingValidator;
    @Mock
    private CreateBookingRequestDto request;
    @Mock
    private BookingMapper bookingMapper;
    @Mock
    private AppUserRepository userRepository;
    @Mock
    private BookingNotificationService bookingNotificationService;

    @Before
    public void setup() {
        initMocksBehavior();
        bookUnitService = new BookingUnitService(
                bookingRepository,
                unitRepository,
                bookingValidator,
                bookingMapper,
                userRepository,
                bookingNotificationService
        );
    }

    @Test(expected = InvalidRequestException.class)
    public void throwInvalidRequestExceptionWhenRequestIsInvalid() {
        doThrow(new InvalidRequestException(INVALID_REQUEST)).when(bookingValidator).validate(request);

        bookUnitService.createFrom(request);
    }

    @Test(expected = UnitNotFoundException.class)
    public void throwUnitNotFoundExceptionWhenUnitDoesNotExist() {
        when(unitRepository.findById(UNIT_ID)).thenReturn(Optional.empty());

        bookUnitService.createFrom(request);
    }

    @Test(expected = UnprocessableRequestException.class)
    public void throwUnprocessableRequestExceptionWhenUnitIsUnavailableWithinTheBookingDates() {
        when(bookingRepository.bookingsWithin(any(), any())).thenReturn(asList(BOOKING));

        bookUnitService.createFrom(request);
    }

    @Test
    public void createANewBooking() {
        bookUnitService.createFrom(request);

        verify(bookingRepository).save(BOOKING);
    }

    @Test
    public void createANotificationWhenCreatingABooking() {
        bookUnitService.createFrom(request);

        verify(bookingNotificationService).createWith(USER.getEmail(), UNIT.getTitle(), BOOKING.getCheckIn());
    }

    private void initMocksBehavior() {
        initMocks(this);
        when(request.getUnitId()).thenReturn(UNIT_ID);
        when(unitRepository.findById(UNIT_ID)).thenReturn(Optional.of(UNIT));
        when(bookingMapper.mapFrom(request, UNIT.getTimezone())).thenReturn(BOOKING);
        when(bookingRepository.save(BOOKING)).thenReturn(BOOKING);
        when(userRepository.findById(BOOKING.getUserId())).thenReturn(Optional.of(USER));
        when(unitRepository.findById(BOOKING.getUnitId())).thenReturn(Optional.of(UNIT));
    }
}
