import sys

def check_balance(filename):
    with open(filename, 'r', encoding='utf-8') as f:
        content = f.read()
    
    stack = []
    lines = content.split('\n')
    for i, line in enumerate(lines):
        for j, char in enumerate(line):
            if char == '{':
                stack.append((i + 1, j + 1, line.strip()))
            elif char == '}':
                if not stack:
                    print(f"Extra '}}' at {i + 1}:{j + 1}")
                else:
                    stack.pop()
    
    if stack:
        print(f"File: {filename}")
        for line_num, col, text in stack:
            print(f"Unclosed '{{' at {line_num}:{col} -> {text}")
    else:
        print(f"File: {filename} - Balanced")

if __name__ == "__main__":
    check_balance(sys.argv[1])
