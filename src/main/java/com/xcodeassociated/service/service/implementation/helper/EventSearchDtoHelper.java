package com.xcodeassociated.service.service.implementation.helper;

import com.xcodeassociated.service.exception.ServiceException;
import com.xcodeassociated.service.exception.codes.ErrorCode;
import com.xcodeassociated.service.model.domain.dto.EventSearchDto;
import com.xcodeassociated.service.utils.Utils;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class EventSearchDtoHelper {
    private EventSearchDtoHelper() {
        throw new ServiceException(ErrorCode.E002, "EventSearchDtoHelper should not be instantiated");
    }

    public static boolean dtoSearchable(EventSearchDto dto) {
        return StringUtils.isNoneBlank(dto.getId())
                || StringUtils.isNoneBlank(dto.getUuid())
                || StringUtils.isNoneBlank(dto.getTitle())
                || StringUtils.isNoneBlank(dto.getCreatedBy())
                || StringUtils.isNoneBlank(dto.getModifiedBy())
                || Objects.nonNull(dto.getCreatedDate())
                || Objects.nonNull(dto.getStart())
                || Objects.nonNull(dto.getStop())
                || Utils.anyNonNull(dto.getLocation())
                || Utils.anyNonNull(dto.getEventCategories());
    }
}
