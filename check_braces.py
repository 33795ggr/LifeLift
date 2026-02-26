import sys

def check_balance(filename):
    with open(filename, 'r', encoding='utf-8') as f:
        content = f.read()
    
    stack = []
    lines = content.split('\n')
    for i, line in enumerate(lines):
        for j, char in enumerate(line):
            if char == '{':
                stack.append((i + 1, j + 1))
            elif char == '}':
                if not stack:
                    print(f"Extra '}}' at {i + 1}:{j + 1}")
                else:
                    stack.pop()
    
    for line, col in stack:
        print(f"Unclosed '{{' at {line}:{col}")

if __name__ == "__main__":
    check_balance(sys.argv[1])
