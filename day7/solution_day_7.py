from functools import reduce
from typing import Dict, List, Optional


def read_lines_from_file(file_path: str) -> List[str]:
    with open(file_path, "r") as f:
        return list(map(str.strip, f.readlines()))


class File:
    def __init__(self, name: str, file_size: int) -> None:
        self.name = name
        self.file_size = file_size


class Directory:
    def __init__(self, name: str, parent_dir: Optional["Directory"]) -> None:
        self.name = name
        self._parent_dir = parent_dir
        self.subdirectories: Dict[str, Directory] = dict()
        self.files: Dict[str, File] = dict()
        self.size = 0

    @staticmethod
    def new_root() -> "Directory":
        return Directory("/", None)

    def update_size(self) -> None:

        for subdirectory in self.subdirectories.values():
            subdirectory.update_size()

        subdirectories_size = sum(
            [
                subdirectory.size
                for subdirectory in self.subdirectories.values()
            ]
        )

        files_size = sum([f.file_size for f in self.files.values()])

        self.size = subdirectories_size + files_size

    def add_file(self, file_size: int, file_name: str) -> None:
        self.files[file_name] = File(file_name, file_size)

    def navigate_to(self, dir_name: str) -> "Directory":
        if dir_name not in self.subdirectories:
            self.subdirectories[dir_name] = Directory(dir_name, self)

        return self.subdirectories[dir_name]

    def parent(self) -> "Directory":
        if self._parent_dir is None:
            return self
        else:
            return self._parent_dir

    def navigate_to_root(self) -> "Directory":
        if self._parent_dir is None:
            return self
        else:
            return self._parent_dir.navigate_to_root()


def interpret_line(line: str, current_directory: Directory) -> Directory:
    if line == "$ cd /":
        return current_directory.navigate_to_root()

    elif line == "$ cd ..":
        return current_directory.parent()

    elif line.startswith("$ cd"):
        target_dir_name = line.split(" ")[-1]
        return current_directory.navigate_to(target_dir_name)

    elif line.split(" ")[0].isnumeric():
        line_elements = line.split(" ")
        current_directory.add_file(int(line_elements[0]), line_elements[1])
        return current_directory

    else:
        return current_directory


def get_all_directories(directory: Directory) -> List[Directory]:
    subdirectory_lists = [
        get_all_directories(subdirectory)
        for subdirectory in directory.subdirectories.values()
    ]

    return [directory] + list(
        reduce(lambda l1, l2: l1 + l2, subdirectory_lists, [])
    )


def load_file_system(lines: List[str]) -> Directory:
    directory = Directory.new_root()

    for line in lines:
        directory = interpret_line(line, directory)

    root = directory.navigate_to_root()

    root.update_size()

    return root


def part_a(lines: List[str]) -> str:
    file_system_root = load_file_system(lines)

    MAX_SIZE = 100_000

    return str(
        sum(
            [
                directory.size
                for directory in get_all_directories(file_system_root)
                if directory.size <= MAX_SIZE
            ]
        )
    )


def part_b(lines: List[str]) -> str:
    file_system_root = load_file_system(lines)

    TOTAL_SPACE = 70_000_000
    REQUIRED_SPACE = 30_000_000
    FREE_SPACE = TOTAL_SPACE - file_system_root.size

    candiate_directories = [
        directory
        for directory in get_all_directories(file_system_root)
        if FREE_SPACE + directory.size >= REQUIRED_SPACE
    ]

    dir_to_delete = min(
        candiate_directories, key=lambda directory: directory.size
    )

    return str(dir_to_delete.size)


if __name__ == "__main__":

    input_text = read_lines_from_file("input_day_7.txt")

    result_a = part_a(input_text)
    result_b = part_b(input_text)

    print(f"Solution one: {result_a}\n" f"Solution two: {result_b}")
