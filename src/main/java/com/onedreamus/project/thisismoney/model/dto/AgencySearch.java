package com.onedreamus.project.thisismoney.model.dto;

import com.onedreamus.project.thisismoney.model.entity.Agency;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AgencySearch {

    private Integer id;
    private String name;

    public static AgencySearch from(Agency agency) {
        return AgencySearch.builder()
                .id(agency.getId())
                .name(agency.getName())
                .build();
    }

}
