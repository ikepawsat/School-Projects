import random

def pivot(arr, low, high):
    i = low - 1
    pi = arr[high]

    for x in range(low, high):
        if arr[x] < pi:
            i += 1
            arr[i], arr[x] = arr[x], arr[i]

    i += 1
    arr[i], arr[high] = arr[high], arr[i]
    return i
        

def quick_sort(arr, low, high):
    if low < high:
        piv = pivot(arr, low, high)

        quick_sort(arr, low, piv - 1)
        quick_sort(arr, piv + 1, high)

def binary_search(arr, target):
    low = 0 
    high = len(arr) - 1

    while low <= high:
        middle = (low + high) // 2
        if arr[middle] < target:
            low = middle + 1
        elif arr[middle] > target:
            high = middle - 1
        else:
            return middle
        
    return -1 #not found

arr = [random.randint(1, 1000) for _ in range(100)]
quick_sort(arr, 0, len(arr) - 1)
print(arr)
target = arr[random.randint(0, len(arr) - 1)]
print(f"{target} found at index {binary_search(arr, target)}")