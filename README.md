# Voice Prompt Download Issue

Repository demostrating a voice skin download issue. Voice skin's won't download directly after initializing HERE. A few second delay seems to remedy the situation.
See `app/src/main/java/dev/williamreed/voice_prompt_fail/MainActivity` for relevant code.

## Note
If running this demo multiple times be sure to clear application data before running it again as to clear previously download data. running repeatedly seems to mitigate the download issue

## Output on first running
```
2020-01-19 11:45:41.950 15257-15257/dev.williamreed.voice_prompt_fail E/MainActivity: Voice download not started initially
2020-01-19 11:45:46.974 15257-15538/dev.williamreed.voice_prompt_fail E/MainActivity: voice downloaded second time
```

## Setup
- Place `HERE-sdk.aar` in the `HERE-sdk` folder.
- Replace the id, token, and license in `app/build.gradle`

