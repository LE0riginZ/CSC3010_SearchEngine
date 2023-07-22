package com.project.lucene.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DocumentItem {
    public String title;
    public String content;
    public String url;

    
    
}