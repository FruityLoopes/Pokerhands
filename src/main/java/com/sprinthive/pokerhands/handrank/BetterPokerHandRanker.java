package com.sprinthive.pokerhands.handrank;

import com.sprinthive.pokerhands.Card;
import com.sprinthive.pokerhands.CardRank;
import com.sprinthive.pokerhands.Suit;

import java.util.*;

public class BetterPokerHandRanker implements HandRanker {

    public HandRank findBestHandRank(List<Card> cards) {
        if (cards.size() != 5) {
            return new NotRankableHandRanker(cards);
        }

        Collections.sort(cards); //first sort cards then reverse the order because we want to start from the highest rank
        Collections.reverse(cards);

        //if statements checking from best hand to worst hand you can get.
        if (isRoyalFlush(cards)) {
            return new RoyalFlushHandRank(cards.get(0).getSuit());
        } else if (isStraightFlush(cards)) {
            return new StraightFlushHandRank(cards.get(0).getRank());
        } else if (isFullHouse(cards)) {
            //initialising  trips and pair
            CardRank trips = null;
            CardRank pair = null;
            Map<CardRank, Integer> rankCount = new HashMap<>(); //creating dic to hold the card ranks
            for (Card card : cards) {
                rankCount.put(card.getRank(), rankCount.getOrDefault(card.getRank(), 0) + 1);//each time it finds a rank of the same it will update the count by 1
            }
            for (Map.Entry<CardRank, Integer> entry : rankCount.entrySet()) {
                if (entry.getValue() == 3) { //checks if the value of the entry is = 3 to if it is, then it's a trip else if it's = 2 then it's a pair
                    trips = entry.getKey();
                } else if (entry.getValue() == 2) {
                    pair = entry.getKey();
                }
            }
            return new FullHouseHandRank(trips, pair);
        } else if (isFourOfAKind(cards)) {
            return new FourOfAKindHandRank(cards.get(1).getRank());
        } else if (isFlush(cards)) {
            return new FlushHandRank(cards);
        } else if (isStraight(cards)) {
            return new StraightHandRank(cards.get(0).getRank());
        } else if (isThreeOfAKind(cards)) {
            return new ThreeOfAKindHandRank(cards.get(2).getRank());
        } else if (isTwoPair(cards)) {
            return new TwoPairHandRank(cards.get(1).getRank(), cards.get(3).getRank(), cards.get(2).getRank());
        } else if (isOnePair(cards)) {
            CardRank pairRank = null;
            List<CardRank> kickers = new ArrayList<>();
            for (int i = 0; i < cards.size() - 1; i++) { //checks to see if there is one pair of same rank
                if (cards.get(i).getRank() == cards.get(i + 1).getRank()) {
                    pairRank = cards.get(i).getRank();
                    break;
                }
            }
            for (Card card : cards) { //adds the 3 cards that were not a pair to kickers
                if (card.getRank() != pairRank) {
                    kickers.add(card.getRank());
                }
            }
            return new OnePairHandRank(pairRank, kickers);
        } else {
            return new HighCardHandRank(cards);
        }
    }

    //methods for all the hands
    private boolean isRoyalFlush(List<Card> cards) {
        return isStraightFlush(cards) && cards.get(0).getRank() == CardRank.ACE;
    }

    private boolean isStraightFlush(List<Card> cards) {
        return isStraight(cards) && isFlush(cards);
    }

    private boolean isFourOfAKind(List<Card> cards) {
        Set<CardRank> rankSet = new HashSet<>();
        for (Card card : cards) {
            rankSet.add(card.getRank());
        }
        return rankSet.size() == 2;
    }

    private boolean isFullHouse(List<Card> cards) {
        //this is exactly like isFullHouse if statement except it just returns weather hasThreeOfaKind and hasPair are true/false
        Map<CardRank, Integer> rankCount = new HashMap<>();
        for (Card card : cards) {
            rankCount.put(card.getRank(), rankCount.getOrDefault(card.getRank(), 0) + 1); //each time it finds a rank of the same it will update the count by 1
        }
        boolean hasThreeOfAKind = false;
        boolean hasPair = false;
        for (int count : rankCount.values()) {
            if (count == 3) {
                hasThreeOfAKind = true;
            } else if (count == 2) {
                hasPair = true;
            }
        }
        return hasThreeOfAKind && hasPair;
    }

    private boolean isFlush(List<Card> cards) {
        Suit suit = cards.get(0).getSuit();
        for (Card card : cards) {
            if (card.getSuit() != suit) { //checks to see if the suits don't match it returns false
                return false;
            }
        }
        return true;
    }

    private boolean isStraight(List<Card> cards) {
        for (int i = 0; i < cards.size() - 1; i++) {
            if (cards.get(i).getRank().ordinal() - 1 != cards.get(i + 1).getRank().ordinal()) { //it gets the value of the highest ranked card -1 then checks
                // to see if it's =  to the next card if it isn't returns false
                return false;
            }
        }
        return true;
    }

    private boolean isThreeOfAKind(List<Card> cards) {
        Map<CardRank, Integer> rankCount = new HashMap<>();
        for (Card card : cards) {
            rankCount.put(card.getRank(), rankCount.getOrDefault(card.getRank(), 0) + 1); //each time it finds a rank that is the same it will update count by 1
        }
        return rankCount.containsValue(3); //if its 3 it would return true otherwise false
    }

    private boolean isTwoPair(List<Card> cards) {
        Map<CardRank, Integer> rankCount = new HashMap<>();
        for (Card card : cards) {
            rankCount.put(card.getRank(), rankCount.getOrDefault(card.getRank(), 0) + 1); //each time it finds a rank of the same it will update the count by 1
        }
        int pairCount = 0;
        for (Integer count : rankCount.values()) {
            if (count == 2) {
                pairCount++;
            }
        }
        return pairCount == 2; //if its 2 it would return true otherwise false
    }

    private boolean isOnePair(List<Card> cards) {
        Set<CardRank> rankSet = new HashSet<>();
        Set<CardRank> sameRanks = new HashSet<>();
        for (Card card : cards) {
            if (!rankSet.add(card.getRank())) { //it checks if a card rank has already been added to rankSet then if that rank has already been added it will add it to sameRanks
                sameRanks.add(card.getRank());
            }
        }
        return sameRanks.size() == 1; //if its 1 it would return true otherwise false
    }

}
