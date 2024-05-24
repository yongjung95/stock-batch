package com.example.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Stock extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String corpCode;

    private String corpName;

    private String dataModifyDate;

    private String stockCode;
}