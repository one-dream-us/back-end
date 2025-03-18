package com.onedreamus.project.thisismoney.model.projection;

import com.onedreamus.project.thisismoney.model.entity.News;

public interface TotalViewCountProjection {

    News getNews();
    Integer getTotalViewCount();

}
