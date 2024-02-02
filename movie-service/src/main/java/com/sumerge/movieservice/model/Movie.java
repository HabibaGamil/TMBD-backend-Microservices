package com.sumerge.movieservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document
public class Movie {
        @Id
        private String id;
        private String title;
        private String description;
        private String posterPath;
        private String backdropPath;
        private Date releaseDate;
        private String voteAverage;
        private String voteCount;
}
