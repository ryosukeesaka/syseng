package jp.sysengineern.learning.Entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourceTitleList {
    private Long courceTitleId;
    private String courceTitleName;
    private String courceTitleDescription;
    private String url;
    private List<CourceDetail> courceDetail;
    private double itemCount;
    private double userCompleteCount;
    private double complete;
    private CourceItem courceItem;
    private CourceItem progressCourceItem;
}
