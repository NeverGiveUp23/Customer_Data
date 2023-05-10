import {
    Button,
    Checkbox,
    Flex,
    FormControl,
    FormLabel,
    Heading,
    Input,
    Link,
    Stack,
    Image,
    Text,
    Box,
    AlertIcon,
    Alert
} from '@chakra-ui/react';
import {useEffect, useState} from 'react';
import {useAuth} from '../context/AuthContext.jsx';
import CreateCustomerForm from "../CreateCustomerForm.jsx";
import {Formik, Form, useField} from 'formik';
import * as Yup from 'yup';
import {useNavigate} from 'react-router-dom';
import {successNotification, errorNotification} from "../../services/Notification.js";

const SignUp = () => {
    const {customer, setCustomerFromToken} = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        if(customer) {
            navigate("/dashboard");
        }
    })


    return (
        <Stack minH={'100vh'} direction={{ base: 'column', md: 'row' }}>
            <Flex p={8} flex={1} align={'center'} justify={'center'}>
                <Stack spacing={4} w={'full'} maxW={'md'}>
                    <Image
                        src={
                            'https://clearcode.cc/wp-content/uploads/2019/11/The-anatomy-of-a-customer-data-platform-CDP-main-image.png'
                        }
                        size={"200px"}
                        alt={"Login Image"}
                    />
                    <Heading fontSize={'2xl'} mb='15'>Register for an account.</Heading>
                    <CreateCustomerForm
                    onSuccess={(token) => {
                        localStorage.setItem("access_token", token);
                        setCustomerFromToken();
                        navigate("/dashboard");
                    }}
                    />
                    <Link
                        color={"facebook.500"} href={"/"}
                    >
                       Have an Account? Login now.
                    </Link>
                </Stack>
            </Flex>
            <Flex
                flex='1'
                p='4'
                flexDirection='column'
                alignItems='center'
                justifyContent='center'
                bgGradient={[
                    'linear(to-b, facebook.100, facebook.600)',
                ]}>

                <Text
                    fontSize={'2xl'}
                    color={'white'}
                    fontWeight={'bold'}
                    mb={5}>
                    Welcome to Customer Data!
                </Text>
                <Image
                    alt={'Login Image'}
                    objectFit={'scale-down'}
                    src={
                        'https://clearcode.cc/wp-content/uploads/2019/05/The-Anatomy-Of-a-Data-Management-Platform-DMP-infographic-main-image.png'
                    }
                />
            </Flex>
        </Stack>
    );

}

export default SignUp;