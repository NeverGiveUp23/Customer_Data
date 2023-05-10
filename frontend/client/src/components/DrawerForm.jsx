import {
    Button,
    useDisclosure,
    Drawer,
    DrawerOverlay,
    DrawerContent,
    DrawerCloseButton,
    DrawerHeader,
    DrawerBody,
    Input,
    DrawerFooter
} from "@chakra-ui/react";
import CreateCustomerForm from "./CreateCustomerForm.jsx";

const AddIcon = () => "+";
const CloseIcon = () => "X";
const DrawerForm = ({ fetchCustomers }) => {
    const { isOpen, onOpen, onClose } = useDisclosure()

    return (
        <>
        <Button leftIcon={<AddIcon/>}
                colorScheme="facebook"
                onClick={onOpen}
        >
            Create Customer
        </Button>

            <Drawer isOpen={isOpen} onClose={onClose} size='lg'>
                <DrawerOverlay />
                <DrawerContent>
                    <DrawerCloseButton />
                    <DrawerHeader>Create your account</DrawerHeader>

                    <DrawerBody>
                        <CreateCustomerForm
                        onSuccess={fetchCustomers}
                        />
                    </DrawerBody>

                    <DrawerFooter>
                        <Button leftIcon={<CloseIcon/>}
                                colorScheme="facebook"
                                onClick={onClose}
                        >
                            Close
                        </Button>
                    </DrawerFooter>
                </DrawerContent>
            </Drawer>

        </>
    )
}

export default DrawerForm;

export const App = () => {

    return (
        <>
            <Button onClick={onOpen}>Open</Button>

        </>
    )
}