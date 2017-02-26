//import app.WebApplication;
//import com.asiainfo.werewolfweb.bean.MessageBoard;
//import com.asiainfo.werewolfweb.bean.Player;
//import com.asiainfo.werewolfweb.bean.TopOneBoard;
//import com.asiainfo.werewolfweb.repository.BoardRepository;
//import com.asiainfo.werewolfweb.repository.PlayerRepository;
//import com.asiainfo.werewolfweb.repository.WereWolfRepository;
//import com.asiainfo.werewolfweb.serivce.WereWolfService;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import java.util.Date;
//import java.util.List;
//
///**
// * @author : huangchen
// * @description :
// * @date : 2017/1/4
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = WebApplication.class)
//@Slf4j
//public class ServiceTest {
//    @Autowired
//    private WereWolfRepository wereWolfRepository;
//
//    @Autowired
//    private WereWolfService wereWolfService;
//
//    @Autowired
//    private PlayerRepository playerRepository;
//
//    @Autowired
//    private BoardRepository boardRepository;
//
//    @Test
//    public void count() {
//        wereWolfRepository.createPlayerStats();
//    }
//
//    @Test
//    public void computeWolfTopOne() {
//        System.out.println(wereWolfService.computeWolfTopOne());
//    }
//
//    @Test
//    public void computeWitchTopOne() {
//        System.out.println(wereWolfService.computeWitchTopOne());
//    }
//
//    @Test
//    public void computeSeerTopOne() {
//        System.out.println(wereWolfService.computeSeerTopOne());
//    }
//
//    @Test
//    public void testBoard() {
//        TopOneBoard topOneBoard = new TopOneBoard();
//        topOneBoard.setBoardId(System.currentTimeMillis() + "");
//        topOneBoard.setPlayerId("test");
//        topOneBoard.setMessage("bbaa");
//        topOneBoard.setName("TESTNAME");
//        topOneBoard.setIsValid("Y");
//        topOneBoard.setInDate(new Date());
//        boardRepository.insertTopOneBoard(topOneBoard);
//
//        TopOneBoard topOneBoardRsp = boardRepository.queryTopOneBoard("test", "wolf");
//        System.out.println(topOneBoardRsp);
//
//        boardRepository.inValidTopOneBoard(topOneBoardRsp.getBoardId());
//    }
//
//    @Test
//    public void testGetBoards() {
//        System.out.println(boardRepository.queryTopOneBoards());
//    }
//
//    @Test
//    public void testBoardAddService() {
//        wereWolfService.addTopOneBoard("maqinqin", "ppaofaew", "wolf");
//    }
//
//    @Test
//    public void testAddMessageBoard() {
//        for (int i = 0; i < 5; i++) {
//            MessageBoard messageBoard = new MessageBoard();
//            messageBoard.setBoardId(System.currentTimeMillis() + "");
//            messageBoard.setPlayerId("test" + i);
//            messageBoard.setMessage("bbaa");
//            messageBoard.setName("TESTNAME" + i);
//            messageBoard.setIsValid("Y");
//            messageBoard.setInDate(new Date());
//            boardRepository.insertMessageBoard(messageBoard);
//        }
//    }
//
//    @Test
//    public void testInValidMessageBoard() {
//        boardRepository.inValidMessageBoard("1487124804319");
//    }
//
//    @Test
//    public void testGetMessageBoard() {
//        System.out.println(boardRepository.queryMessageBoards(2));
//    }
//
//    @Test
//    public void testCountByVillagerKilled() {
//        List<Player> players = playerRepository.getPlayers();
//        for (Player player : players) {
//            System.out.println(player.getName() + ": " + wereWolfRepository.countByVillagerKilled(player.getPlayerId()));
//        }
//    }
//
//    @Test
//    public void testCountBySeerFisrNightKilled() {
//        List<Player> players = playerRepository.getPlayers();
//        for (Player player : players) {
//            System.out.println(player.getName() + ": " + wereWolfRepository.countBySeerFisrNightKilled(player.getPlayerId()));
//        }
//    }
//
//    @Test
//    public void testCountByHuntPlayer() {
//        List<Player> players = playerRepository.getPlayers();
//        for (Player player : players) {
//            System.out.println(player.getName() + ": " + wereWolfRepository.countByHunted(player.getPlayerId()));
//        }
//    }
//
//}
