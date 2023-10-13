import socket
from netaddr import IPNetwork

# files
input_file = "ip-ranges.txt"
output_file = "ip-list.txt"

# delete line from file
def delete_range(rangeFile: str, rangeLine: str):
    lines = []
    for line in open(rangeFile):
        if not line.startswith(rangeLine):
            lines.append(line)

    with open(rangeFile, 'w') as file:
        file.writelines(lines)

# check if ssh open
def is_port_open(ip, port, timeout=1):
    try:
        socket.setdefaulttimeout(timeout)
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.connect((ip, port))
        s.close()
        return True
    except (socket.error, socket.timeout):
        return False

# calculate ips
def calculate_ip_list(input_file, output_file):
    with open(input_file, "r") as f1:
        for line in f1:
            cidr = line.strip()
            try:
                ip_network = IPNetwork(cidr)
                for ip in ip_network:
                    if is_port_open(str(ip), 22):
                        with open(output_file, "a") as f2:
                            f2.write(str(ip) + "\n")
                        print("\033[36mTesting: " + str(ip) + " -> "  + "range: " + line.rstrip('\n') + " = \033[32mOpen")
                    else:
                        print("\033[36mTesting: " + str(ip) + " -> "  + "range: " + line.rstrip('\n') + " = \033[31mClosed")
                # delete line
                delete_range(input_file, line)
            except Exception as e:
                print(f"Chyba při zpracování rozsahu {cidr}: {e}")

# init main function
calculate_ip_list(input_file, output_file)
