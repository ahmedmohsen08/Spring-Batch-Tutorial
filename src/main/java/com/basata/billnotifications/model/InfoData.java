package com.basata.billnotifications.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoData {
    private String source;
    private String title;
    private String content;
    private UUID deviceId;
}
