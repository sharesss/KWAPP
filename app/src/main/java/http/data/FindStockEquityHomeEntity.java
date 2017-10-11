package http.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by A1 on 2017/9/11.
 */

public class FindStockEquityHomeEntity {
    private int id;//股权转让信息id
    private int userId;//股权转让发布者id
    private String stockName;//项目名称
    private int stockIndustryId;//所属行业id
    private String industryName;//所属行业名称
    private String financingState;//融资状态
    private String leadAgency;//领头机构/人
    private int returnWay;//回报方式：1定期分红，2股权增值
    private String exitWay;//退出机制
    private String stockDesc;//股权介绍
    private double stockSellRate;//出让股份比例
    private int startingPrice;//起拍价
    private int priceRisingRate;//加价幅度
    private int transactionPrice;//成交价
    private String time;//起始倒计时auctionStartTime
    private Long auctionStartTime;
    private Long auctionEndTime;
    private String branch;//fen
    private String second;//miao
    private int auctionState;//竞拍状态：0未开始，1竞拍成功，2竞拍失败
    private String forthose;//股权拍得者
    private int compere;//主持人
    private String chatRoomId;
    private int isFirst;
    private int auditState;//审核状态：0未审核，1审核通过，2审核失败
    private String auditDesc;//审核原因/失败
    private int attentionNum;//关注数
    private String photoUrl;//图片
    private int topPrice;
    private int reserveNum;
    private int isApply;
    private int isRemind;//是否提醒 0未提醒 ，1已提醒
    private String  forthoseName;
    private String forthoseHeadPic;
    //个人信息
    private int isinvestauthen;//0,不是1，投资人，2，大v
    private String nickname;
    private String  headpic;
    private String position;
    private String company;
    private int followNum;
    private int makeOverNum;
    private int auctionNum;
    private String  disclaimer;
    private RankingEntity info;
    private ArrayList <String> arr = new ArrayList<>();
    ArrayList<RankingEntity> rankingarr= new ArrayList<>();
    public FindStockEquityHomeEntity(JSONObject jsonObj) {
        this.id = jsonObj.optInt("id", 0);
        this.userId = jsonObj.optInt("userId",0);
        this.stockName = jsonObj.optString("stockName","");
        this.stockIndustryId = jsonObj.optInt("stockIndustryId",0);
        this.industryName = jsonObj.optString("industryName");
        this.financingState = jsonObj.optString("financingState");
        this.leadAgency = jsonObj.optString("leadAgency");
        this.returnWay = jsonObj.optInt("returnWay",0);
        this.stockDesc = jsonObj.optString("stockDesc");
        this.exitWay = jsonObj.optString("exitWay");
        this.stockSellRate = jsonObj.optDouble("stockSellRate");
        this.startingPrice = jsonObj.optInt("startingPrice",0);
        this.priceRisingRate = jsonObj.optInt("priceRisingRate",0);
        this.transactionPrice = jsonObj.optInt("transactionPrice",0);
        this.auctionState = jsonObj.optInt("auctionState",0);
        this.forthose = jsonObj.optString("forthose");
        this.compere = jsonObj.optInt("compere",0);
        this.isFirst = jsonObj.optInt("isFirst",0);
        this.auditState = jsonObj.optInt("auditState",0);
        this.attentionNum = jsonObj.optInt("attentionNum",0);
        this.auditDesc = jsonObj.optString("auditDesc");
        this.disclaimer = jsonObj.optString("disclaimer");
        this.chatRoomId = jsonObj.optString("chatRoomId");
        this.isApply = jsonObj.optInt("isApply",0);
        this.isRemind = jsonObj.optInt("isRemind",0);
        this.forthoseName = jsonObj.optString("forthoseName");
        this.forthoseHeadPic = jsonObj.optString("forthoseHeadPic");
        this.auctionEndTime = jsonObj.optLong("auctionEndTime");
        this.auctionStartTime = jsonObj.optLong("currentTime");
        //解析个人信息
            try {
                if (!jsonObj.isNull("information")) {
                JSONObject js =  jsonObj.getJSONObject("information");
                 this.nickname = js.optString("nickname","");
                    this.isinvestauthen = js.optInt("isinvestauthen",0);
                    this.headpic = js.optString("headpic","");
                    this.company = js.optString("company","");
                    this.position = js.optString("position");
                    this.followNum = js.optInt("followNum",0);
                    this.makeOverNum = js.optInt("makeOverNum",0);
                    this.auctionNum = js.optInt("auctionNum",0);
                }
                //解析照片
                if (!jsonObj.isNull("photoPresen1")) {
                    JSONArray array = jsonObj.optJSONArray("photoPresen1");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject js = array.getJSONObject(i);
                            String photoUrl = js.optString("photoUrl");
                            arr.add(photoUrl);
                            this.photoUrl =  arr.get(0);
                        }

                }
                //解析照片
                if (!jsonObj.isNull("bidRecords")) {
                    JSONArray array = jsonObj.optJSONArray("bidRecords");
                    for (int i = 0; i < 2; i++) {
                        JSONObject js = array.getJSONObject(i);
                        info = new RankingEntity(js);

                        rankingarr.add(info);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public int getStockIndustryId() {
        return stockIndustryId;
    }

    public void setStockIndustryId(int stockIndustryId) {
        this.stockIndustryId = stockIndustryId;
    }

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public String getFinancingState() {
        return financingState;
    }

    public void setFinancingState(String financingState) {
        this.financingState = financingState;
    }

    public String getLeadAgency() {
        return leadAgency;
    }

    public void setLeadAgency(String leadAgency) {
        this.leadAgency = leadAgency;
    }

    public int getReturnWay() {
        return returnWay;
    }

    public void setReturnWay(int returnWay) {
        this.returnWay = returnWay;
    }

    public String getExitWay() {
        return exitWay;
    }

    public void setExitWay(String exitWay) {
        this.exitWay = exitWay;
    }

    public String getStockDesc() {
        return stockDesc;
    }

    public void setStockDesc(String stockDesc) {
        this.stockDesc = stockDesc;
    }

    public double getStockSellRate() {
        return stockSellRate;
    }

    public void setStockSellRate(double stockSellRate) {
        this.stockSellRate = stockSellRate;
    }

    public int getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(int startingPrice) {
        this.startingPrice = startingPrice;
    }

    public int getPriceRisingRate() {
        return priceRisingRate;
    }

    public void setPriceRisingRate(int priceRisingRate) {
        this.priceRisingRate = priceRisingRate;
    }

    public int getTransactionPrice() {
        return transactionPrice;
    }

    public void setTransactionPrice(int transactionPrice) {
        this.transactionPrice = transactionPrice;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public int getAuctionState() {
        return auctionState;
    }

    public void setAuctionState(int auctionState) {
        this.auctionState = auctionState;
    }

    public String getForthose() {
        return forthose;
    }

    public void setForthose(String forthose) {
        this.forthose = forthose;
    }

    public int getCompere() {
        return compere;
    }

    public void setCompere(int compere) {
        this.compere = compere;
    }

    public int getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(int isFirst) {
        this.isFirst = isFirst;
    }

    public int getAuditState() {
        return auditState;
    }

    public void setAuditState(int auditState) {
        this.auditState = auditState;
    }

    public String getAuditDesc() {
        return auditDesc;
    }

    public void setAuditDesc(String auditDesc) {
        this.auditDesc = auditDesc;
    }

    public int getAttentionNum() {
        return attentionNum;
    }

    public void setAttentionNum(int attentionNum) {
        this.attentionNum = attentionNum;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getIsinvestauthen() {
        return isinvestauthen;
    }

    public void setIsinvestauthen(int isinvestauthen) {
        this.isinvestauthen = isinvestauthen;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getFollowNum() {
        return followNum;
    }

    public void setFollowNum(int followNum) {
        this.followNum = followNum;
    }

    public int getMakeOverNum() {
        return makeOverNum;
    }

    public void setMakeOverNum(int makeOverNum) {
        this.makeOverNum = makeOverNum;
    }

    public int getAuctionNum() {
        return auctionNum;
    }

    public void setAuctionNum(int auctionNum) {
        this.auctionNum = auctionNum;
    }

    public int getTopPrice() {
        return topPrice;
    }

    public void setTopPrice(int topPrice) {
        this.topPrice = topPrice;
    }

    public int getReserveNum() {
        return reserveNum;
    }

    public void setReserveNum(int reserveNum) {
        this.reserveNum = reserveNum;
    }

    public ArrayList<String> getArr() {
        return arr;
    }

    public void setArr(ArrayList<String> arr) {
        this.arr = arr;
    }

    public RankingEntity getInfo() {
        return info;
    }

    public void setInfo(RankingEntity info) {
        this.info = info;
    }

    public ArrayList<RankingEntity> getRankingarr() {
        return rankingarr;
    }

    public void setRankingarr(ArrayList<RankingEntity> rankingarr) {
        this.rankingarr = rankingarr;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public class RankingEntity {
        String nickName;
        String headPic;
        int bidTime;
        int topBidPrice;
        public RankingEntity(JSONObject js){
            this.nickName = js.optString("nickName");
            this.headPic= js.optString("headPic");
            this.bidTime = js.optInt("bidTime",0);
            this.topBidPrice = js.optInt("topBidPrice",0);

        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getHeadPic() {
            return headPic;
        }

        public void setHeadPic(String headPic) {
            this.headPic = headPic;
        }

        public int getBidTime() {
            return bidTime;
        }

        public void setBidTime(int bidTime) {
            this.bidTime = bidTime;
        }

        public int getTopBidPrice() {
            return topBidPrice;
        }

        public void setTopBidPrice(int topBidPrice) {
            this.topBidPrice = topBidPrice;
        }
    }

    public int getIsApply() {
        return isApply;
    }

    public void setIsApply(int isApply) {
        this.isApply = isApply;
    }

    public int getIsRemind() {
        return isRemind;
    }

    public void setIsRemind(int isRemind) {
        this.isRemind = isRemind;
    }

    public String getForthoseName() {
        return forthoseName;
    }

    public void setForthoseName(String forthoseName) {
        this.forthoseName = forthoseName;
    }

    public String getForthoseHeadPic() {
        return forthoseHeadPic;
    }

    public void setForthoseHeadPic(String forthoseHeadPic) {
        this.forthoseHeadPic = forthoseHeadPic;
    }

    public Long getAuctionStartTime() {
        return auctionStartTime;
    }

    public void setAuctionStartTime(Long auctionStartTime) {
        this.auctionStartTime = auctionStartTime;
    }

    public Long getAuctionEndTime() {
        return auctionEndTime;
    }

    public void setAuctionEndTime(Long auctionEndTime) {
        this.auctionEndTime = auctionEndTime;
    }
}
