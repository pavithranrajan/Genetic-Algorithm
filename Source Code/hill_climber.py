#Hill Climber Implementation

#Import Necessary libraries
from random import randint


#The target string to be evolved
target_string = "Welcome to CS547!"
#Possible characters in the string
population = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890 !"

# calculate a given individual
def evaluate(indv):
    fitness = 0
    for i in range(len(indv)):
        if indv[i] == target_string[i]:
            fitness += 1
    return fitness

#Generate random string
def random_string(population, length):
    ret_val = ""
    for i in range(length):
        rand = randint(0, len(population) -1)
        ret_val += population[rand]
    return ret_val


# Find a path from a initial string to final string
def hill_climber(initial_string, final_string):
    cur_iter = initial_string
    pos = 0
    iteration = 0

    #search till the initial string is evolved to final string
    while cur_iter != final_string:
        try:
            # if the current string is close to final string move the pointer
            while cur_iter[pos] == final_string[pos]:
                pos += 1
                if pos == len(target_string):
                    return iteration
        except IndexError:
            cur_iter += " "

        # Generate neighbours
        neighbour = {}

        for i in range(len(population)):
            insert = cur_iter[:pos] + population[i]
            neighbour[insert] = evaluate(insert)

        # get best neighbours
        cur_iter = sorted(neighbour.items(), key=lambda x: x[1], reverse=True)[:1][0][0]

        iteration += 1
    return iteration


def main():

    #Generate random string
    initial_string = random_string(population, len(target_string))

    #call the hill climber function with initial string and target string
    evolve = hill_climber(initial_string, target_string)

    #Print final number of iterations to reach the target string
    print("No of Iterations: " + str(evolve))


if __name__ == "__main__":
    main()
