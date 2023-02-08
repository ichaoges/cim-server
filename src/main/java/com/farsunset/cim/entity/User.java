package com.farsunset.cim.entity;

import lombok.Builder;
import lombok.Data;

/**
 * @author ichaoge
 */
@Data
@Builder
public class User {

    private Long id;

    private String name;

    private String telephone;
}
