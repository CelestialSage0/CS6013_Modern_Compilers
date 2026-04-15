import json
import re

def parse_chat(text):
    pattern = r"You said\n(.*?)\nGemini said\n(.*?)(?=\nYou said|\Z)"
    matches = re.findall(pattern, text, re.DOTALL)

    messages = []

    for user_msg, assistant_msg in matches:
        messages.append({
            "role": "user",
            "content": user_msg.strip()
        })
        messages.append({
            "role": "assistant",
            "content": assistant_msg.strip()
        })

    return messages


def main():
    with open("gemini.txt", "r", encoding="utf-8") as f:
        text = f.read()

    messages = parse_chat(text)

    output = {
        "platform": "gemini",
        "messages": messages
    }

    with open("chat.json", "w", encoding="utf-8") as f:
        json.dump(output, f, indent=2, ensure_ascii=False)

    print("✅ Clean JSON exported to chat.json")


if __name__ == "__main__":
    main()
