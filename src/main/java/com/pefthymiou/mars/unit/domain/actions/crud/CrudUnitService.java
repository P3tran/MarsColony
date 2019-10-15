package com.pefthymiou.mars.unit.domain.actions.crud;

import com.pefthymiou.mars.booking.infrastructure.db.BookingRepository;
import com.pefthymiou.mars.unit.domain.*;
import com.pefthymiou.mars.unit.infrastructure.db.Unit;
import com.pefthymiou.mars.unit.infrastructure.db.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class CrudUnitService implements CrudService {

    private static final String UNIT_NOT_FOUND_MESSAGE = "Requested unit was not found";
    private static final String CANNOT_DELETE_UNIT_WITH_BOOKINGS = "The unit cannot be deleted, because there are bookings for it";
    private static final String TITLE_EXISTS = "This title already exist";

    private UnitRepository unitRepository;
    private UnitValidator unitValidator;
    private UnitMapper unitMapper;
    private BookingRepository bookingRepository;

    @Autowired
    public CrudUnitService(UnitRepository unitRepository, UnitValidator unitValidator, UnitMapper unitMapper, BookingRepository bookingRepository) {
        this.unitRepository = unitRepository;
        this.unitValidator = unitValidator;
        this.unitMapper = unitMapper;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Unit retrieve(long id) {
        Optional<Unit> unit = unitRepository.findById(id);

        if (unit.isPresent())
            return unit.get();
        else
            throw new UnitNotFoundException(UNIT_NOT_FOUND_MESSAGE);
    }

    @Override
    public long createFrom(RequestDto request) {
        unitValidator.validate(request);
        checkIfTitleExists((CreateUnitRequestDto) request);
        Unit unit = unitMapper.mapFrom((CreateUnitRequestDto) request);
        Unit savedUnit = unitRepository.save(unit);
        return savedUnit.getId();
    }

    @Override
    public void deleteById(long id) {
        checkIfUnitExists(id);
        checkIfUnitHasBookings(id);
        unitRepository.deleteById(id);
    }

    @Override
    public void update(long id, Map updatedFields) {
        unitValidator.validateForUpdate(updatedFields);
        Optional<Unit> unit = unitRepository.findById(id);

        if (unit.isPresent()) {
            Unit updatedUnit = unitMapper.mapForUpdateFrom(unit.get(), updatedFields);
            unitRepository.save(updatedUnit);
        }
        else
            throw new UnitNotFoundException(UNIT_NOT_FOUND_MESSAGE);
    }

    private void checkIfTitleExists(CreateUnitRequestDto request) {
        if (unitRepository.existsByTitle(request.getTitle())) {
            throw new UnprocessableRequestException(TITLE_EXISTS);
        }
    }

    private void checkIfUnitExists(long id) {
        if (!unitRepository.existsById(id))
            throw new UnitNotFoundException(UNIT_NOT_FOUND_MESSAGE);
    }

    private void checkIfUnitHasBookings(long id) {
        if (bookingRepository.existsByUnitId(id))
            throw new UnprocessableRequestException(CANNOT_DELETE_UNIT_WITH_BOOKINGS);
    }
}
