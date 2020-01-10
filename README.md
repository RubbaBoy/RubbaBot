# RubbaBot

RubbaBot is an experimental project in attempt to train the 1558M model gpt-2 Neural Network on over 132,000 messages by only myself on Discord.

## Setup Instructions

1. Scrape your messages via [DiscordChatExporter](https://github.com/Tyrrrz/DiscordChatExporter) in the CSV setting.
2. Run `com.uddernetworks.rubbabot.MessageFilter` to filter out the messages with the ID constant in the file to create an `output.txt` file. Alternatively, put messages to train on in an `output.txt`
3. Set up GPT-2 1558M via:
   1. `git clone https://github.com/RubbaBoy/gpt-2.git`
   2. `./gpt-2/setup.sh 1558M output.txt`
   3. `./gpt-2/train.sh`

