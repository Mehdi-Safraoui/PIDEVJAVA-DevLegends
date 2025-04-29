from typing import Any, Text, Dict, List
from rasa_sdk import Action, Tracker
from rasa_sdk.executor import CollectingDispatcher
import random

class ActionRecommendExercise(Action):

    def name(self) -> Text:
        return "action_recommend_exercise"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        exercises = [
            "Fais une séance de respiration profonde pendant 5 minutes.",
            "Essaye une méditation guidée rapide disponible sur YouTube.",
            "Fais quelques étirements doux pour relâcher les tensions.",
            "Va marcher 10 minutes à l'extérieur et observe la nature."
        ]

        suggestion = random.choice(exercises)

        dispatcher.utter_message(text=f"Voici un exercice pour toi : {suggestion}")
        return []
