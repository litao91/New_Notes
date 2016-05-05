# Descriptors

| What                      | Type             | Name                             | Description   |
| ------------------------- | :-------------- | :------------------------------- | :------------ |
| Memory Descriptor         | `mm_struct`      | `mm` field of process descriptor |               |
| Memory Region Descriptor  | `vm_area_struct` | `mm_map` field of mm_struct      |               |

## The Memory Descriptor
| Type                      | Field    | Description                                                             |
| :----                     | :--      | :--                                                                     |
| `struct vm_area_struct *` | `mmap`   | Pointers to the head of the list of memory region objects               |
| `struct rb_root`          | `mm_rb`  | Pointer to the root of the rb tree of memory region objects             |
| `struct list_head`        | `mmlist` | adjacent mm_struct (memory descriptor) in the list of memory descriptor |
| `atomic_t`                | mm_users |                                                                         |
| `atomic_t`                | mm_count |                                                                         |

### Fields
* first element of the `mmlist` is the `mmlist` field of `init_mm`  -- used by process 0
* The `mmlist` is protected by `mmlist_lock`
* `mm_users` -- lightweight processes' that share the `mm_struct` data structure. 
* `mm_count` -- main usage counter --  All "users" in `mm_user` count as one unit in `mm_count`

### Methods
`mm_alloc()`
* Allocate a new **memory descriptor**
    - Stored in a slab allocator => `mm_alloc` calls `kmem_cache_alloc()`

`mmput()`
* dscreases the `mm_users`
* if `mm_users` becomes 0
    * releases the Local Descriptor table
    * The memory region descriptors
    * Page Tables referenced by the memory descriptor
    * invokes mmdrop

`mmdrop()`
* decreases `mm_count`
* releases the `mm_struct` data structure if `mm_count` becomes zero. 
