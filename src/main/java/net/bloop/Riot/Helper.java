package net.bloop.Riot;

import no.stelar7.api.l4j8.basic.constants.types.GameQueueType;
import no.stelar7.api.l4j8.basic.constants.types.SeasonType;
import no.stelar7.api.l4j8.basic.utils.LazyList;
import no.stelar7.api.l4j8.pojo.match.MatchReference;
import no.stelar7.api.l4j8.pojo.match.ParticipantStats;
import no.stelar7.api.l4j8.pojo.staticdata.champion.StaticChampion;
import no.stelar7.api.l4j8.pojo.summoner.Summoner;

import java.util.*;

public class Helper {

    private List<Integer> championIds = new ArrayList<>();
    public List<MatchReference> matches;
    public Summoner summoner;

    public Helper()
    {
    }

    public void updateSummoner(Summoner summonerIn)
    {
        Set<SeasonType> season = new HashSet<>();
        season.add(SeasonType.SEASON_2019);
        summoner = summonerIn;
        matches = summonerIn.getGames().withSeasons(season).getLazy();
        System.out.println("Loading fully...");
        ((LazyList<MatchReference>) matches).loadFully();
        System.out.println("Done. Loaded " + matches.size() + " games in season 2019.");
    }

    public int getChampionFrequency(int champion, Map<Integer, StaticChampion> champData)
    {
        int timesPlayed = 0;
        for(int i = 1; i <= matches.size(); i++)
        {
            int championId = matches.get(i-1).getChampionId();
            championIds.add(championId);

            if(championId == champion)
                timesPlayed++;
        }

        return timesPlayed;
    }

    public int mostFrequentChampAsId()
    {
        Integer[] arr = championIds.toArray(new Integer[championIds.size()]);

        Arrays.sort(arr);

        int count = 1, tempCount;
        int popular = arr[0];
        int temp = 0;
        for (int i = 0; i < (arr.length - 1); i++)
        {
            temp = arr[i];
            tempCount = 0;
            for (int j = 1; j < arr.length; j++)
            {
                if (temp == arr[j])
                    tempCount++;
            }
            if (tempCount > count)
            {
                popular = temp;
                count = tempCount;
            }
        }

        return popular;
    }

    public long getIntedGames()
    {
        long inted = 0;
        for(int i = 1; i <= matches.size(); i++)
        {
            long kda;
            ParticipantStats stats = matches.get(i-1).getFullMatch().getParticipantFromSummonerId(summoner.getSummonerId()).getStats();
            if(stats.getDeaths() != 0)
                kda = (stats.getKills() + stats.getAssists()) / stats.getDeaths();
            else
                kda = 1000000000; //yay perfect kda good job friend!
            if(kda < 1)
                inted++;
        }
        return inted;
    }

    public String getFriendlyMatchName(GameQueueType type)
    {
        //Thanks to discord user Klospülautomat#6249 for the switch
        switch (type) {
            case CUSTOM:                            return "Custom Game";
            case FIRSTBLOOD_1X1:                    return "Snowdown Showdown 1v1";
            case FIRSTBLOOD_2X2:                    return "Snowdown Showdown 2v2";
            case HEXAKILL:                          return "Hexakill";
            case HEXAKILL_6X6_SR:                   return "Ultra Rapid Fire";
            case ONE_FOR_ALL_MIRROR:                return "One For All: Mirror Mode";
            case BOT_URF_5X5:                       return "Co-op vs AI Ultra Rapid Fire";
            case BILGEWATER_ARAM_5X5:               return "ARAM";
            case COUNTER_PICK:                      return "Nemesis";
            case BILGEWATER_5X5:                    return "Black Market Brawlers";
            case DEFINITELY_NOT_DOMINION_5X5:       return "Definitely Not Dominion";
            case ARAM_5X5:                          return "All Random";
            case TEAM_BUILDER_DRAFT_UNRANKED_5X5:   return "Draft Pick";
            case TEAM_BUILDER_RANKED_SOLO:          return "Ranked Solo/Duo";
            case NORMAL_5V5_BLIND_PICK:             return "Blind Pick";
            case RANKED_FLEX_SR:                    return "Ranked Flex";
            case ARAM:                              return "ARAM";
            case NORMAL_3X3_BLIND_PICK:             return "Blind Pick 3v3";
            case RANKED_FLEX_TT:                    return "Ranked Flex 3v3";
            case ASSASSINATE_5X5:                   return "Blood Hunt Assassin";
            case DARKSTAR_3X3:                      return "Dark Star: Singularity";
            case CLASH:                             return "Clash";
            case BOT_3X3_INTERMEDIATE:              return "Co-op vs AI Intermediate Bot";
            case BOT_3X3_INTRO:                     return "Co-op vs AI Intro Bot";
            case BOT_3X3_BEGINNER:                  return "Co-op vs AI Beginner Bot";
            case BOT_5X5_INTRO:                     return "Co-op vs AI Intro Bot";
            case BOT_5X5_BEGINNER:                  return "Co-op vs AI Beginner Bot";
            case BOT_5X5_INTERMEDIATE:              return "Co-op vs AI Intermediate Bot";
            case ALL_RANDOM_URF:                    return "ARURF";
            case ASCENSION_5X5:                     return "Ascension";
            case KING_PORO_5X5:                     return "Legend of the Poro King";
            case NEXUS_SIEGE:                       return "Nexus Siege";
            case NIGHTMARE_BOT_5X5_VOTE:            return "Doom Bots Voting";
            case NIGHTMARE_BOT_5X5:                 return "Doom Bots";
            case INVASION_NORMAL:                   return "Star Guardian Invasion: Normal";
            case INVASION_ONSLAUGHT:                return "Star Guardian Invasion: Onslaught";
            case OVERCHARGE:                        return "PROJECT: Hunters";
            case SNOW_BATTLE_ARURF:                 return "ARURF";
            case ONEFORALL_5X5:                     return "One For All";
            case ODYSSEY_INTRO:                     return "Odyssey Extraction: Intro";
            case ODYSSEY_CADET:                     return "Odyssey Extraction: Cadet";
            case ODYSSEY_CREWMEMBER:                return "Odyssey Extraction: Crewmember";
            case ODYSSEY_CAPTAIN:                   return "Odyssey Extraction: Captain";
            case ODYSSEY_ONSLAUGHT:                 return "Odyssey Extraction: Onslaught";
            default:                                return "Unknown";
        }
    }
}
