import { IConfig } from 'app/shared/model/config.model';

export interface ISmartObject {
    id?: number;
    type?: string;
    shared?: boolean;
    fabriquant?: string;
    yearOfFabriquation?: string;
    userLogin?: string;
    userId?: number;
    configs?: IConfig[];
}

export class SmartObject implements ISmartObject {
    constructor(
        public id?: number,
        public type?: string,
        public shared?: boolean,
        public fabriquant?: string,
        public yearOfFabriquation?: string,
        public userLogin?: string,
        public userId?: number,
        public configs?: IConfig[]
    ) {
        this.shared = this.shared || false;
    }
}
