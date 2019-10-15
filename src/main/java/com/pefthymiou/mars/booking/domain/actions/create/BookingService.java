package com.pefthymiou.mars.booking.domain.actions.create;

import com.pefthymiou.mars.unit.domain.RequestDto;
import com.pefthymiou.mars.unit.domain.Resource;

public interface BookingService<T extends Resource> {

    long createFrom(RequestDto request);
}
