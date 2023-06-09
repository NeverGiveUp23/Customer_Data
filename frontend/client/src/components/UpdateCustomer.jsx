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
    DrawerFooter,
    FormLabel,
    Select,
    Stack,
    Alert,
    AlertIcon,
    Box,
    VStack,
    Image
} from "@chakra-ui/react";
import * as Yup from 'yup';
import {Form, Formik, useField} from 'formik';
import CreateCustomerForm from "./CreateCustomerForm.jsx";
import {updateCustomer, uploadCustomerPicture, getCustomerProfilePictureUrl} from "../services/client.jsx";
import React, {useCallback} from 'react'
import {useDropzone} from 'react-dropzone'
import {successNotification, errorNotification} from "../services/Notification.js";



const MyTextInput = ({label, ...props}) => {
    // useField() returns [formik.getFieldProps(), formik.getFieldMeta()]
    // which we can spread on <input>. We can use field meta to show an error
    // message if the field is invalid and it has been touched (i.e. visited)
    const [field, meta] = useField(props);
    return (
        <Box>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Input className="text-input" {...field} {...props} />
            {meta.touched && meta.error ? (
                <Alert className="error" status={"error"} mt={2}>
                    <AlertIcon/>
                    {meta.error}
                </Alert>
            ) : null}
        </Box>
    );
};

const MySelect = ({label, ...props}) => {
    const [field, meta] = useField(props);
    return (
        <Box>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Select {...field} {...props} />
            {meta.touched && meta.error ? (
                <Alert className="error" status={"error"} mt={2}>
                    <AlertIcon/>
                    {meta.error}
                </Alert>
            ) : null}
        </Box>
    );
};

const AddIcon = () => "+";
const CloseIcon = () => "X";



// Profile Image Upload component

const MyDropzone = ({ id , fetchCustomers }) => {
    const onDrop = useCallback(acceptedFiles => {

        const formData = new FormData();
        formData.append("file", acceptedFiles[0]);

        uploadCustomerPicture(
            id,
            formData
        ).then(res => {
            successNotification("Picture uploaded successfully", "Picture uploaded successfully")
            fetchCustomers()
        }).catch(err => {
            errorNotification("Failed to upload picture", "Failed to upload picture")
        })
    }, [])




    const {getRootProps, getInputProps, isDragActive} = useDropzone({onDrop})

    return (
        <Box {...getRootProps()}
             w={"100%"}
             textAlign={"center"}
             border={"dashed"}
             borderColor={"gray.200"}
             borderRadius={"3xl"}
             p={6}
             rounded={"md"}
        >
            <input {...getInputProps()} />
            {
                isDragActive ?
                    <p>Drop the picture here</p> :
                    <p>Drag 'n' drop some picture here, or click to select picture</p>
            }
        </Box>
    )
}



const UpdateCustomer = ({id, name, email, age, gender, fetchCustomers}) => {
    const { isOpen, onOpen, onClose } = useDisclosure()
    return (
        <>
            <>
                <Button leftIcon={<AddIcon/>}
                        colorScheme="facebook"
                        onClick={onOpen}
                >
                    Update
                </Button>

                <Drawer isOpen={isOpen} onClose={onClose} size='lg'>
                    <DrawerOverlay />
                    <DrawerContent>
                        <DrawerCloseButton />
                        <DrawerHeader>Update Customer</DrawerHeader>

                        <DrawerBody>
                            <Formik
                                initialValues={{
                                    name: name,
                                    email: email,
                                    age: age,
                                    gender: gender,
                                }}
                                validationSchema={Yup.object({
                                    name: Yup.string()
                                        .max(15, 'Must be 15 characters or less')
                                        .required('Required'),
                                    email: Yup.string()
                                        .email('Must be 20 characters or less')
                                        .required('Required'),
                                    age: Yup.number()
                                        .min(16, 'Must be at least 16 years of age')
                                        .max(100, 'Must be less than 100 years of age')
                                        .required()
                                })}
                                onSubmit={(updatedCustomer, {setSubmitting}) => {
                                    setSubmitting(true);
                                    updateCustomer(id, updatedCustomer)
                                        .then(res => {
                                            console.log(res);
                                            successNotification(
                                                "Customer Updated",
                                                `${updatedCustomer.name} was successfully Updated!`
                                            )
                                            fetchCustomers();
                                        }).catch(err => {
                                        console.log(err);
                                        errorNotification(
                                            err.response.data.title,
                                            err.response.data.message
                                        )
                                    }).finally(() => {
                                        setSubmitting(false);
                                    })
                                }}
                            >
                                {({isValid, isSubmitting, dirty}) => (

                                    <Form>
                                        <VStack
                                       spacing={'5'}
                                       mb={'5'}
                                        >
                                            <Image
                                            borderRadius={'full'}
                                            boxSize={'150px'}
                                            objectFit={'cover'}
                                              src={getCustomerProfilePictureUrl(id)}
                                            />
                                            <MyDropzone id={id} fetchCustomers={fetchCustomers}/>
                                        </VStack>

                                        <Stack spacing={"24px"}>
                                            <MyTextInput
                                                label="Name"
                                                name="name"
                                                type="text"
                                                placeholder="Jane"
                                            />

                                            <MyTextInput
                                                label="Email Address"
                                                name="email"
                                                type="email"
                                                placeholder="jane@formik.com"
                                            />

                                            <MyTextInput
                                                label="Age"
                                                name="age"
                                                type="number"
                                                placeholder="20"
                                            />

                                            <Button isDisabled={!(isValid && dirty) || isSubmitting} type="submit">Submit</Button>
                                        </Stack>
                                    </Form>
                                )}
                            </Formik>
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
        </>
    )
}

export default UpdateCustomer;