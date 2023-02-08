package com.farsunset.cim.entity;

import lombok.Builder;
import lombok.Data;

/**
 * @author ichaoge
 */
@Data
@Builder
public class TempUser {

    private Long id;

    private String name;

    private String telephone;
}
