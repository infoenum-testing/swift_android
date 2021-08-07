package com.swiftdating.app.model.requestmodel;

public class ProposeTimeRequestModel {
//    "matchId": 0,
//  "time": "YYYY-MM-DD HH:MM:SS"

    private String matchId="";String time="";

    public ProposeTimeRequestModel(String matchId, String time) {
        this.matchId = matchId;
        this.time = time;
    }


    public String getMatchId() {
        return matchId;
    }

    public String getTime() {
        return time;
    }
}
