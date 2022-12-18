from typing import List, Tuple, Iterable
from itertools import islice
from functools import reduce

def get_compartments(line: str) -> Tuple[str, str]:

    pivot = len(line) // 2

    return line[:pivot], line[pivot:]


def get_common_item(strings: Iterable[str]) -> str:

    item_sets = [set(string) for string in strings]

    return reduce(set.intersection, item_sets).pop()


def get_item_prio(item: str) -> int:
    if item.islower():
        return ord(item) - 96
    else:
        return ord(item) - 38


def group_into_three(lines: List[str]) -> List[Tuple[str, str, str]]:
    return list(
        islice(
            zip(lines, lines[1:], lines[2:]), 
            None, 
            None, 
            3
        )
    )


def get_intersecting_prio_sum(windows: Iterable[Iterable[str]]) -> int:

    common_items = [get_common_item(window) for window in windows]
    priorities = [get_item_prio(item) for item in common_items]

    return sum(priorities)


def run_one(lines: List[str]) -> int:
    
    rucksacks = [get_compartments(line) for line in lines]
    
    return get_intersecting_prio_sum(rucksacks)


def run_two(lines: List[str]) -> int:
    
    groups = group_into_three(lines)

    return get_intersecting_prio_sum(groups)
    

def read_lines_from_file(file_path: str) -> List[str]:
    with open(file_path, "r") as f:
        return list(map(str.strip, f.readlines()))


if __name__ == "__main__":

    input_text = read_lines_from_file("input_day_3.txt")
    
    solution_one = run_one(input_text)
    solution_two = run_two(input_text)

    print(
        f"Solution one: {solution_one}\n"
        f"Solution two: {solution_two}"
    )
