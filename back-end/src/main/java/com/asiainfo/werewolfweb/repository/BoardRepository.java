package com.asiainfo.werewolfweb.repository;

import com.asiainfo.werewolfweb.bean.MessageBoard;
import com.asiainfo.werewolfweb.bean.TopOneBoard;
import com.asiainfo.werewolfweb.utils.IConstant;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

/**
 * @author : huangchen
 * @description :
 * @date : 2017/2/14
 */
@Repository
public class BoardRepository {
    private final MongoOperations operations;

    @Autowired
    public BoardRepository(MongoOperations operations) {
        this.operations = operations;
    }


    public static MongoDatabase getMongoDatabase() {
        MongoClientURI connectionString = new MongoClientURI("mongodb://127.0.0.1:27017");
        MongoClient mongoClient = new MongoClient(connectionString);
        MongoDatabase database = mongoClient.getDatabase("werewolf");
        return database;
    }

    /**
     * 增加topone榜单留言
     */
    public void insertTopOneBoard(TopOneBoard topOneBoard) {
        operations.insert(topOneBoard);
    }

    /**
     * 获取指定玩家有效的topone榜单留言
     */
    public TopOneBoard queryTopOneBoard(String playerId, String type) {
        Query query = query(
                where("playerId").is(playerId)
                        .and("isValid").is(IConstant.ValidTag.VALID)
                        .and("type").is(type));
        return operations.findOne(query, TopOneBoard.class);
    }

    /**
     * 获取所有有效的topone榜单留言
     */
    public List<TopOneBoard> queryTopOneBoards() {
        Query query = query(where("isValid").is(IConstant.ValidTag.VALID));
        return operations.find(query, TopOneBoard.class);
    }

    /**
     * 失效指定的topone榜单留言
     */
    public void inValidTopOneBoard(String boardId) {
        Query query = query(where("boardId").is(boardId));
        Update update = update("isValid", IConstant.ValidTag.IN_VAILD);
        operations.updateFirst(query, update, TopOneBoard.class);
    }


    /**
     * 增加message留言
     */
    public void insertMessageBoard(MessageBoard messageBoard) {
        operations.insert(messageBoard);
    }

    /**
     * 获取指定数量有效的message留言
     */
    public List<MessageBoard> queryMessageBoards(Integer limit) {
        Sort sort = new Sort(Sort.Direction.DESC, "inDate");
        Query query = query(where("isValid").is(IConstant.ValidTag.VALID)).with(sort).limit(limit);
        return operations.find(query, MessageBoard.class);
    }

    /**
     * 失效指定的message榜单留言
     */
    public void inValidMessageBoard(String boardId) {
        Query query = query(where("boardId").is(boardId));
        Update update = update("isValid", IConstant.ValidTag.IN_VAILD);
        operations.updateFirst(query, update, MessageBoard.class);
    }
}
