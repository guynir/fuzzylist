import type {Metadata} from "next";
import {Inter} from "next/font/google";
import "./globals.css";
import React from "react";
import {PrimeReactProvider} from 'primereact/api';
import Tailwind from 'primereact/passthrough/tailwind';

const inter = Inter({subsets: ["latin"]});

export const metadata: Metadata = {
    title: "FuzzyList",
    description: "A place for strange and curious lists!",
};

export default function RootLayout({children,}: Readonly<{ children: React.ReactNode; }>) {
    return (
        <html lang="en">
        <body className={inter.className}>
        <PrimeReactProvider value={{unstyled: true, pt: Tailwind}}>
            {children}
        </PrimeReactProvider>
        </body>
        </html>
    );
}
