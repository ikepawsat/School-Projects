import random

def pivot(arr, low, high):
    i = low - 1
    for x in range(low, high):
        if arr[x] < arr[high]:
            i += 1
            arr[i], arr[x] = arr[x], arr[i]

    i += 1
    arr[i], arr[high] = arr[high], arr[i]
    return i

def quicksort(arr, low, high):
    if low < high:
        piv = pivot(arr, low, high)

        quicksort(arr, low, piv - 1)
        quicksort(arr, piv + 1, high)

def binary_search(arr, target):
    low = 0
    high = len(arr) - 1
    while low <= high:
        middle = (high + low) // 2
        if arr[middle] < target:
            low = middle + 1
        elif arr[middle] > target:
            high = middle - 1
        else:
            return middle
    
    return -1 #not found

arr = [random.randint(1, 999) for _ in range(1000)]
quicksort(arr, 0, len(arr) - 1)
print(arr)
target = random.randint(1, 999)
print(f"{target} found at position {binary_search(arr, target)}")