#!/usr/bin/python

# script for calculate ip-list from ranges
# input file ip-ranges.txt -> ip-list.txt

from netaddr import *
import socket

# check if port open
def ping_server(server: str, port: int, timeout=1):
    """ping server"""
    try:
        socket.setdefaulttimeout(timeout)
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.connect((server, port))
    except OSError as error:
        return False
    else:
        s.close()
        return True


# delete line from file
def delete_range(rangeFile: str, rangeLine: str):
    lines = []
    for line in open(rangeFile):
        if not line.startswith(rangeLine):
            lines.append(line)

    with open(rangeFile, 'w') as file:
        file.writelines(lines)

with open("ip-ranges.txt") as f1, open("ip-list.txt", "w") as f:
    for line in f1:
        cidr = line.rstrip('\n')
        ip = IPNetwork(cidr)
        for addr in ip:

            # check if ssh open
            if ping_server(str(addr), 22, 1):
               print("\033[36mTesting: " + str(addr) + " -> "  + " range: " + line.rstrip('\n') + " = \033[32mOpen")
               f.write(str(addr) + '\n')
            else:
                print("\033[36mTesting: " + str(addr) + " -> "  + " range: " + line.rstrip('\n') + " = \033[31mClosed")

        # delete used line
        delete_range("ip-ranges.txt", line.rstrip('\n'))