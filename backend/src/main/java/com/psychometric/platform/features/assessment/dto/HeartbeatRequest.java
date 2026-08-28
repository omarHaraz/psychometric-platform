package com.psychometric.platform.features.assessment.dto;

import java.util.List;

public class HeartbeatRequest {
    private List<ResponseDto> responses;
    public List<ResponseDto> getResponses() { return responses; }
    public void setResponses(List<ResponseDto> responses) { this.responses = responses; }

    public static class ResponseDto {
        private Long itemId;
        private Integer selectedLikert;
        private List<String> rankingOrder;
        private String selectedOption;
        private Long responseTimeMs;

        public Long getItemId() { return itemId; }
        public void setItemId(Long itemId) { this.itemId = itemId; }
        public Integer getSelectedLikert() { return selectedLikert; }
        public void setSelectedLikert(Integer selectedLikert) { this.selectedLikert = selectedLikert; }
        public List<String> getRankingOrder() { return rankingOrder; }
        public void setRankingOrder(List<String> rankingOrder) { this.rankingOrder = rankingOrder; }
        public String getSelectedOption() { return selectedOption; }
        public void setSelectedOption(String selectedOption) { this.selectedOption = selectedOption; }
        public Long getResponseTimeMs() { return responseTimeMs; }
        public void setResponseTimeMs(Long responseTimeMs) { this.responseTimeMs = responseTimeMs; }
    }
}
