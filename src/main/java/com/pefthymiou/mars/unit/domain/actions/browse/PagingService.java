package com.pefthymiou.mars.unit.domain.actions.browse;

import com.pefthymiou.mars.unit.domain.Resource;
import org.springframework.data.domain.Page;

public interface PagingService<T extends Resource> {

    Page<T> browse(int page, int size);
}
