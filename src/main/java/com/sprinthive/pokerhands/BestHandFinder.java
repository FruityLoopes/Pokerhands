package com.sprinthive.pokerhands;

import com.sprinthive.pokerhands.handrank.BetterPokerHandRanker;
import com.sprinthive.pokerhands.handrank.HandRank;
import java.util.ArrayList;
import java.util.List;

public class BestHandFinder {

    public HandRank findBestHand(List<Card> cards) { //find best hand
        List<List<Card>> combinations = generateCombinations(cards, 5); //generates all possible combinations
        BetterPokerHandRanker ranker = new BetterPokerHandRanker();
        HandRank bestHand = null;

        for (List<Card> combination : combinations) { //goes through all combinations and runs findBestHandRank to get the hand rank
            HandRank handRank = ranker.findBestHandRank(combination);
            if (bestHand == null || handRank.compareTo(bestHand) > 0) { //compares if the current hand is better than the new one, if it is it will update bestHand
                bestHand = handRank;
            }
        }

        return bestHand;
    }

    private List<List<Card>> generateCombinations(List<Card> cards, int combinationSize) {
        List<List<Card>> combinations = new ArrayList<>();
        generateCombinationsHelper(cards, combinationSize, 0, new ArrayList<>(), combinations);
        return combinations;
    }

    private void generateCombinationsHelper(List<Card> cards, int combinationSize, int startIndex, List<Card> currentCombination, List<List<Card>> combinations) {
        if (combinationSize == 0) { //if the size is 0 that means it is valid and will add the combination to the best
            combinations.add(new ArrayList<>(currentCombination));
            return;
        }

        for (int i = startIndex; i <= cards.size() - combinationSize; i++) { //goes through cards always starting from startIndex for cards size - combinationSize
            currentCombination.add(cards.get(i));//adds to currentCombination to then run  generateCombinationsHelper, with updated params
            generateCombinationsHelper(cards, combinationSize - 1, i + 1, currentCombination, combinations);
            currentCombination.remove(currentCombination.size() - 1); //removes the last added card to find best combination
        }
    }
}