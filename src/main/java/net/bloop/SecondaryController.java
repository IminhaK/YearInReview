package net.bloop;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import net.bloop.Riot.Helper;
import no.stelar7.api.l4j8.basic.constants.api.Platform;
import no.stelar7.api.l4j8.basic.constants.types.GameQueueType;
import no.stelar7.api.l4j8.basic.constants.types.LaneType;
import no.stelar7.api.l4j8.impl.raw.ImageAPI;
import no.stelar7.api.l4j8.pojo.match.*;
import no.stelar7.api.l4j8.pojo.staticdata.champion.StaticChampion;
import no.stelar7.api.l4j8.pojo.summoner.Summoner;

public class SecondaryController implements Initializable {

    Platform platform = Platform.NA1;
    int mostFrequent;
    Helper helper = new Helper();

    @FXML
    private ImageView pfpImage;
    @FXML
    private Text text1;
    @FXML
    private Text text2;
    @FXML
    private Text text3;
    @FXML
    private Text text4;
    @FXML
    private Text text5;
    @FXML
    private Text text6;

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources){
        platform = Platform.fromString(PrimaryController.getCombo()).get();

        Summoner summoner = Summoner.byName(platform, PrimaryController.getUsername());
        Map<Integer, StaticChampion> champData = App.api.getDDragonAPI().getChampions();
        //pfp as url
        String pfp = ImageAPI.getInstance().getProfileIcon(platform, PrimaryController.getUsername());
        //pfpImage = new ImageView(pfp);
        pfpImage.setImage(new Image(pfp));
        //name and lv
        Integer level = summoner.getSummonerLevel();
        String name = summoner.getName();
        //most recent game
        List<MatchReference> matches = summoner.getGames().get();
        MatchReference recentGame = matches.stream().max(Comparator.comparing(MatchReference::getTimestamp)).get();
        Match match = recentGame.getFullMatch();

        Participant self = match.getParticipantFromSummonerId(summoner.getSummonerId()); //game data for user (summs, champ etc)
        StaticChampion champion = champData.get(recentGame.getChampionId());
        MatchPerks summs = self.getPerks();
        boolean win = match.didWin(self);
        String won = win ? "won" : "lost";

        helper.updateSummoner(summoner);

        ParticipantIdentity opponentId = match.getLaneOpponentIdentity(self); //get lane opponent id

        boolean hasRoles = match.getGameQueueType() == GameQueueType.NORMAL_5X5_DRAFT || match.getGameQueueType() == GameQueueType.RANKED_SOLO_5X5 || match.getGameQueueType() == GameQueueType.RANKED_FLEX_SR;

        text1.setText(name + ", Level " + level);

        text2.setText(name + " " + won + " their most recent " + helper.getFriendlyMatchName(match.getGameQueueType()) + " game as " + champion.getName() + ".");

        text3.setText("");
        if(opponentId != null && match.getGameQueueType() != GameQueueType.ARAM){

            Participant opponent = match.getParticipantFromParticipantId(opponentId.getParticipantId()); //summs, champ, etc for lane opponent
            StaticChampion opponentChamp = champData.get(opponent.getChampionId());
            LaneType role = self.getTimeline().getLane();

            if(hasRoles)
                text3.setText("They were playing " + role + " against " + opponentChamp.getName() + ".");
            else
                text3.setText("They were playing against " + opponentChamp.getName() + ".");
        }

        text4.setText(name + " has played " + champion.getName() + " " + helper.getChampionFrequency(champion.getId(), champData) + " times this season.");
        //label5 = new JLabel(name + " has played " + champData.get(107).getName() + " " + helper.getChampionFrequency(107, champData) + " times this season."); //FOR TESTING A SPECIFIC CHAMPION

        mostFrequent = helper.mostFrequentChampAsId();

        text5.setText("Their most popular champion this season is " + champData.get(mostFrequent).getName() + " and they have " + String.format("%,d", summoner.getChampionMastery(mostFrequent).getChampionPoints()) + " points on them.");
        if(champion.getId() != mostFrequent)
            text5.setText(text5.getText() + " They have played " + champData.get(mostFrequent).getName()+ " " + helper.getChampionFrequency(mostFrequent, champData) + " times.");

        //long inted = helper.getIntedGames();
        //text6.setText("They have inted (less than 1.0 KDA) in " + inted + " out of " + matches.size() + " games.");
    }
}