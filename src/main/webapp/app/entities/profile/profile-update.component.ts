import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IProfile } from 'app/shared/model/profile.model';
import { ProfileService } from './profile.service';
import { IUser, UserService } from 'app/core';
import { IProfileConfValue } from 'app/shared/model/profile-conf-value.model';
import { ProfileConfValueService } from 'app/entities/profile-conf-value';

@Component({
    selector: 'jhi-profile-update',
    templateUrl: './profile-update.component.html'
})
export class ProfileUpdateComponent implements OnInit {
    profile: IProfile;
    isSaving: boolean;

    users: IUser[];

    profileconfvalues: IProfileConfValue[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected profileService: ProfileService,
        protected userService: UserService,
        protected profileConfValueService: ProfileConfValueService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ profile }) => {
            this.profile = profile;
        });
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.profileConfValueService.query().subscribe(
            (res: HttpResponse<IProfileConfValue[]>) => {
                this.profileconfvalues = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.profile.id !== undefined) {
            this.subscribeToSaveResponse(this.profileService.update(this.profile));
        } else {
            this.subscribeToSaveResponse(this.profileService.create(this.profile));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IProfile>>) {
        result.subscribe((res: HttpResponse<IProfile>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    trackProfileConfValueById(index: number, item: IProfileConfValue) {
        return item.id;
    }
}
