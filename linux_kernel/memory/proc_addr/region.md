# Memory Regions
Type `vm_area_struct`

Some fields

| Type                            | Field      | Description                                      |
| :-                              | :-         | :-                                               |
| `struct mm_struct *`            | `vm_mm`    | Pointer to the memory descriptor owns the region |
| `unsigned long`                 | `vm_start` | First linear address inside the region           |
| `unsigned long`                 | `vm_end`   | First linear address after the region            |
| `struct vm_area_struct *`       | `vm_next`  | next region in the process list.                 |
| `struct vm_operations_struct *` | `vm_ops`   | Pointers to the methods of the memory region     |

Each memory region descriptor identifies a linear address interval. 
=> from `vm_start` to `vm_end`
`vm_end` - `vm_start` = length of the memory region. 

When a new range of of linear addresses is added to the process address
space, the kernel checks whether an already existing memory region can be
changed (**case a**). 
If not, a new memory region is created (**case b**). 

It's similar if a range of linear address is removed. (**case c**: resize the
memory region, **case d**: force a memory region to split.)

## Operations in `vm_operation_struct`
| Method   | Description                                                                      |
| :-       | :-                                                                               |
| open     | Add memory to the set of regions                                                 |
| close    | remove region                                                                    |
| nopage   | Invoked by Page Fault exception handler                                          |
| populate | Set page table entries corresponding to the linear address of the memory region. |

To Search the memory region that includes a specific linear address:
* Red-block tree.
* The head of RB-tree is referenced by the `mm_rb` field of the memory
descriptor.

# Memory Region Handling
Low-level fuctions that operate on memory region descriptors
* `do_mmap()` -- enlarge teh address space of a process
* `do_munmap()` -- shrink the address space of a process

Aux functions


