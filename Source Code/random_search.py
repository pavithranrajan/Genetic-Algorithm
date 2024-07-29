#Random Search Implementation

#Import Necessary libraries
from random import randint

#The target string to be evolved
target_string = "Welcome to CS547!"
#Possible characters in the string
population = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890 !"

#Generate random string
def random_string(population, length):
    ret_val = ""
    for i in range(length):
        rand = randint(0, len(population) -1)
        ret_val += population[rand]
    return ret_val

def main():

    max_steps = 20000

    cur_string = ""
    attempt = 0

    # Evaluate fitness
    while cur_string != target_string and max_steps>0:
        cur_string = random_string(population, len(target_string))
        print(cur_string)
        attempt += 1

        print(attempt)
        max_steps = max_steps -1

        


if __name__ == "__main__":
    main()
