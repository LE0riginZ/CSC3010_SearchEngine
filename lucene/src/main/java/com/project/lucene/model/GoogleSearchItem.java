package com.project.lucene.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class GoogleSearchItem {
    public String title;
    public String url;
    public String position;

    
    
}