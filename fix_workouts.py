import sys

filename = r'C:\Users\killmilk\Desktop\1313\LifeLift\app\src\main\java\com\lifelift\app\features\workouts\WorkoutsScreen.kt'
with open(filename, 'r', encoding='utf-8') as f:
    lines = f.readlines()

new_lines = []
found = False
for i, line in enumerate(lines):
    if 'state = dismissState,' in line and not found:
        # Insert declaration before the current line (which is inside SwipeToDismissBox)
        # Actually SwipeToDismissBox starts on previous line
        # Let's find SwipeToDismissBox
        j = i
        while j > 0 and 'SwipeToDismissBox(' not in lines[j]:
            j -= 1
        
        indent = lines[j][:lines[j].find('SwipeToDismissBox')]
        declaration = [
            f"{indent}val dismissState = rememberSwipeToDismissBoxState(\n",
            f"{indent}    confirmValueChange = {{ dismissValue ->\n",
            f"{indent}        if (dismissValue == SwipeToDismissBoxValue.EndToStart) {{\n",
            f"{indent}            workoutToDelete = workout\n",
            f"{indent}            showDeleteDialog = true\n",
            f"{indent}            false\n",
            f"{indent}        }} else {{\n",
            f"{indent}            false\n",
            f"{indent}        }}\n",
            f"{indent}    }}\n",
            f"{indent})\n",
            "\n"
        ]
        # Insert declaration before SwipeToDismissBox
        new_lines = new_lines[:j] + declaration + new_lines[j:]
        new_lines.append(line.replace('state = dismissState,', 'state = dismissState,')) # Keep the line as is since we added dismissState before
        found = True
    else:
        new_lines.append(line)

with open(filename, 'w', encoding='utf-8') as f:
    f.writelines(new_lines)
print("Fixed WorkoutsScreen.kt")
