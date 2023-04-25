import React from 'react';
import {
    AlertDialog,
    AlertDialogBody,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogContent,
    AlertDialogOverlay,
    useDisclosure,
    Button
} from '@chakra-ui/react'
import { deleteCustomer } from "../services/client.jsx";
import { errorNotification, successNotification} from "../services/notification.js";
function DeleteCustomer({ id, name, fetchCustomers }) {
    const { isOpen, onOpen, onClose } = useDisclosure()
    const cancelRef = React.useRef()

    return (
        <>
            <Button
                colorScheme='red'
                onClick={onOpen}
                _hover={{
                    transform: 'translateY(-2px)',
                    boxShadow: 'lg',
                    m: '3px'
                }}
            >
                Delete
            </Button>

            <AlertDialog
                isOpen={isOpen}
                leastDestructiveRef={cancelRef}
                onClose={onClose}
            >
                <AlertDialogOverlay>
                    <AlertDialogContent>
                        <AlertDialogHeader fontSize='lg' fontWeight='bold'>
                            Delete Customer
                        </AlertDialogHeader>

                        <AlertDialogBody>
                            Are you sure? You can't undo this action afterwards. This will permanently delete {name}
                        </AlertDialogBody>

                        <AlertDialogFooter>
                            <Button ref={cancelRef} onClick={onClose}>
                                Cancel
                            </Button>
                            <Button
                                colorScheme='red'
                                onClick={() => {
                                    deleteCustomer(id).then(() => {
                                        successNotification(
                                            'Customer Deleted',
                                            `${name} has been deleted.`
                                        )
                                        fetchCustomers();
                                    }).catch(err => {
                                        console.log(err);
                                        errorNotification(
                                          err.code,
                                          err.response.data.message
                                        );
                                    })
                                    onClose();
                                }}
                                ml={3}

                            >
                                Delete
                            </Button>
                        </AlertDialogFooter>
                    </AlertDialogContent>
                </AlertDialogOverlay>
            </AlertDialog>
        </>
    )
}

export default DeleteCustomer;